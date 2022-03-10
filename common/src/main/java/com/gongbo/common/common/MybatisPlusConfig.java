package com.gongbo.common.common;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.gongbo.common.common.entity.CommonVal1440View;
import com.gongbo.common.dynamicresult.DynamicResult;
import com.gongbo.common.utils.ClassScanUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MybatisPlusConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 由于mybatis-plus只会在entity、mapper等都存在时，才会往TableInfoHelper中添加TableInfo，
     * 故在此手动往TableInfoHelper添加TableInfo
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Class<?>> classes = ClassScanUtils.scan(
                new String[]{"com.goldwind.pf.minidata.entity.*", "com.goldwind.pf.minidata.common.entity.*"},
                //保留不为接口，且实现DynamicResult接口的类
                clazz -> {
                    //过滤接口，只对实体类有效
                    if (clazz.isInterface()) {
                        return false;
                    }

                    //要求必须实现DynamicResult接口
                    if (!DynamicResult.class.isAssignableFrom(clazz)) {
                        return false;
                    }

                    //mybatis-plus没有初始化过的类
                    return TableInfoHelper.getTableInfo(clazz) == null;
                });

        if (CollUtil.isEmpty(classes)) {
            return;
        }

        TableInfo tableInfo = TableInfoHelper.getTableInfo(CommonVal1440View.class);
        MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(tableInfo.getConfiguration(), "");
        builderAssistant.setCurrentNamespace(tableInfo.getCurrentNamespace());
        for (Class<?> clazz : classes) {
            TableInfoHelper.initTableInfo(builderAssistant, clazz);
        }
    }
}
