package com.qiyi.zuul.remote.feign;

import com.qiyi.common.base.CurrentAccount;
import com.qiyi.common.interceptor.ServiceFeignInterceptor;
import com.qiyi.zuul.remote.feign.failure.IAccountResFeignFailure;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "grace-admin",configuration = ServiceFeignInterceptor.class,fallback = IAccountResFeignFailure.class)
public interface IAccountResFeign {

    @GetMapping(name = "根据token获取用户信息",value = "/account/info/{token}")
    public CurrentAccount getAccountInfoByToken(@PathVariable(name = "token")String token);
}
