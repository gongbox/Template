package com.gongbo.util;

import com.gongbo.common.result.Result;
import com.gongbo.common.result.ResultCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Checks {
    /**
     * 验证请求结果是否成功
     */
    public static void checkResultCode(Result<?> result) {
        Assertions.assertEquals(result.getCode(), ResultCode.SUCCESS.getCode());
    }
}
