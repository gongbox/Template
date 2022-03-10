package com.gongbo.common.dynamictable.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.gongbo.common.dynamictable.core.DynamicTableHelper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态表配置类
 */
public class DynamicTableConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Setter(onMethod_ = @Autowired(required = false))
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (mybatisPlusInterceptor == null) {
            return;
        }
        Map<String, TableNameHandler> tableNameHandlers = DynamicTableHelper.getTableNameHandlers();
        if (CollUtil.isEmpty(tableNameHandlers)) {
            return;
        }
        //设置动态表配置
        List<InnerInterceptor> interceptors = new ArrayList<>();
        //必须在分页插件之前
        interceptors.add(new DynamicTableNameInnerInterceptor(tableNameHandlers));
        interceptors.addAll(mybatisPlusInterceptor.getInterceptors());
        mybatisPlusInterceptor.setInterceptors(interceptors);
    }
}
