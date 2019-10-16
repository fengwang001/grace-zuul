package com.qiyi.zuul.filter;


import javax.servlet.http.HttpServletRequest;

import com.qiyi.common.base.JwtToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qiyi.zuul.constant.HeaderConstant;
import com.qiyi.zuul.constant.TokenConstant;

public class LoginFilter extends ZuulFilter{
	
	@Override
	public Object run() throws ZuulException {
		  //JWT
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //token对象,有可能在请求头传递过来，也有可能是通过参数传过来，实际开发一般都是请求头方式
        String token = request.getHeader(HeaderConstant.HEADER_TOKEN.getValue());

        if (StringUtils.isBlank((token))) {
            token = request.getParameter(HeaderConstant.HEADER_TOKEN.getValue());
        }
        //登录校验逻辑  如果token为null，则直接返回客户端，而不进行下一步接口调用
        if (StringUtils.isBlank(token)) {
            // 过滤该请求，不对其进行路由
            requestContext.setSendZuulResponse(false);
            //返回错误代码
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }else {
			if(JwtToken.jwtTokenRefresh()){ // 验证通过
				requestContext.setSendZuulResponse(true);
			}else { // token 过期
				requestContext.setSendZuulResponse(false);
	            //返回错误代码
	            requestContext.setResponseStatusCode(TokenConstant.TOKEN_OUT.getValue());
			}
		}
        return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //完整路径接口
        String url = request.getRequestURI();
        String local_url = "/api/admin/auth/jwt/token";
        /**
         * 如果是登录接口不进行token验证
         */
        if (url.startsWith(local_url)){
            return false;
        }else{
            return true;
        }

	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     *
	 * @return
	 */

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}
}
