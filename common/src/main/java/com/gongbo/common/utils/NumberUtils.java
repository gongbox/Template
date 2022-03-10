package com.gongbo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * 数字工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtils {

    /**
     * 转换为Optional<BigDecimal>
     */
    public static Optional<BigDecimal> parseOptionalBigDecimal(Object obj) {
        return Optional.ofNullable(parseBigDecimal(obj));
    }

    /**
     * 转换为BigDecimal
     */
    public static BigDecimal parseBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        } else if (obj instanceof Number) {
            return new BigDecimal(String.valueOf(obj));
        } else if (obj instanceof String) {
            if (StringUtils.isEmpty(obj)) {
                return null;
            }
            return new BigDecimal((String) obj);
        } else {
            throw new NumberFormatException(MessageFormat.format("Format Exception,不能格式化该类型:{0}", obj.getClass().getName()));
        }
    }
}
