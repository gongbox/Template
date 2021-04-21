package com.gongbo.common.validate.custom.interceptor;

import com.gongbo.common.validate.custom.validator.RangeCheckContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidateInterceptor implements HandlerInterceptor {

    /**
     * 清空
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RangeCheckContext.clear();
    }

}
