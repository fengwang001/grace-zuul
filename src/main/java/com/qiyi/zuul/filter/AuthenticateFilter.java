package com.qiyi.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qiyi.common.base.CurrentAccount;
import com.qiyi.common.jwt.JwtConstant;
import com.qiyi.zuul.config.ZuulSecurityConfig;
import com.qiyi.zuul.exception.NonLoginException;
import com.qiyi.zuul.remote.feign.IAccountResFeign;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;


import java.time.LocalDateTime;

public class AuthenticateFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateFilter.class);

    @Autowired
    private ZuulSecurityConfig config;

    @Autowired
    private IAccountResFeign accountResFeign;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestUri = request.getRequestURI();
        String prefix = config.getPrefix();
        if(prefix != null && requestUri.indexOf(prefix) > -1)
            requestUri = requestUri.substring(prefix.length());
        if(isStartWith(requestUri)){
            requestContext.addZuulRequestHeader("verify","N");
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = request.getHeader(config.getSecurity().getTokenHeader());
        String entity = request.getHeader("Entity");
        System.out.println("-----token:" + token);
        if(StringUtils.isBlank(token)){
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            setFailedRequest(JSON.toJSONString(new NonLoginException("token 为空，请重新登录")),HttpStatus.SC_UNAUTHORIZED);
            return null;
        }
        /**
         * 根据token换取信息
         */
        CurrentAccount tokenUser = null;
//        if(!"newer".equals(entity)){
             tokenUser = accountResFeign.getAccountInfoByToken(token);
             if(tokenUser == null){
                 setFailedRequest(JSON.toJSONString(new NonLoginException("当前token不是有效的，请重新登录")),HttpStatus.SC_UNAUTHORIZED);
                 return null;
             }
            System.out.println("------"+token.toString());
            // token 过期
            if(tokenUser != null && tokenUser.getExpire() != null && tokenUser.getExpire().isBefore(LocalDateTime.now())){
                setFailedRequest(JSON.toJSONString(new NonLoginException("用户身份过期，请重新登录")),HttpStatus.SC_UNAUTHORIZED);
                return null;
            }
            try {
                // 标识是否通过校验 ，给其他过滤器提供判断依据(T : 通过；F：不通过，N:不需要校验)
                context.addZuulRequestHeader("verify","T");
                context.addZuulRequestHeader(JwtConstant.SERVER_JWT_HEADER,objectMapper.writeValueAsString(tokenUser));
                context.addZuulRequestHeader(config.getSecurity().getTokenHeader(),token);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//        }

        return null;
    }

    /**
     * 判断是否以什么开头
     * @param requestUir
     * @return
     */
    private boolean isStartWith(String requestUir){
        boolean flag = false;
        for (String s:config.getSecurity().getIgnoredPaths()){
            s = StringUtils.substringBefore(s,"/**");
            if(requestUir.startsWith(s)){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 网关抛异常
     * @param body
     * @param code
     */
    private void setFailedRequest(String body,int code){
        RequestContext context = RequestContext.getCurrentContext();
        context.setResponseStatusCode(code);
        if(context.getResponseBody() != null){
            context.setResponseBody(body);
        }
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
    }
}
