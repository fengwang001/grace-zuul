package com.qiyi.zuul.remote.feign.failure;

import com.qiyi.common.base.CurrentAccount;
import com.qiyi.zuul.remote.feign.IAccountResFeign;
import org.springframework.stereotype.Component;

@Component
public class IAccountResFeignFailure implements IAccountResFeign {
    @Override
    public CurrentAccount getAccountInfoByToken(String token) {
        return new CurrentAccount();
    }
}
