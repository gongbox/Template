package com.gongbo.common.batch;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

public class CustomSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertOrUpdateBatchMethod());
        methodList.add(new ReplaceMethod());
        methodList.add(new ReplaceBatchMethod());
        methodList.add(new InsertOnDuplicateKeyUpdateMethod());
        return methodList;
    }
}
