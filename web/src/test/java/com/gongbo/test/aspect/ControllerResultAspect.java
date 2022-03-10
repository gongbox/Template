package com.gongbo.test.aspect;

import com.gongbo.common.result.Result;
import com.gongbo.common.utils.WebUtils;
import com.gongbo.util.Checks;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 针对Controller的响应结果进行检查
 */
@Component
@Aspect
public class ControllerResultAspect {

    @Pointcut("execution(com.gongbo.common.result.Result *(..))")
    public void resultAspect() {
    }

    /**
     * 检查响应Code是否正常
     */
    @AfterReturning(value = "resultAspect()", returning = "result")
    public void afterReturn(Result<?> result) {
        if ("excel".equals(WebUtils.getRequest().getParameter("export"))) {
            return;
        }
        //检查响应Code是否正常
        Checks.checkResultCode(result);
    }
}
