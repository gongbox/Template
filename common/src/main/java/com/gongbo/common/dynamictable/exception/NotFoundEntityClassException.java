package com.gongbo.common.dynamictable.exception;

/**
 * 无法找到Entity类型报错
 */
public class NotFoundEntityClassException extends RuntimeException {

    public NotFoundEntityClassException(String message) {
        super(message);
    }

}
