package com.gongbo.common.dynamicresult;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

/**
 * 自定义扩展类，扩展mybatis默认的对象工厂
 */
public class DynamicResultObjectFactory extends DefaultObjectFactory {

    @Override
    public <T> T create(Class<T> type) {
        if (type == DynamicResult.class) {
            //获取返回结果类型
            type = (Class<T>) DynamicResultHolder.getResultType();
            if (type == null) {
                throw new IllegalStateException("返回动态结果时请先设置返回结果类型");
            }
        }

        return super.create(type);
    }
}