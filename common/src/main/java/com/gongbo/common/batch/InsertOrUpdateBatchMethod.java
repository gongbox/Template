/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gongbo.common.batch;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.gongbo.common.batch.annotations.OnDuplicateUpdate;
import lombok.NoArgsConstructor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

/**
 * 批量插入数据(参考InsertBatchSomeColumn.class)
 *
 * @author gongbo
 * @since 2021-02-06
 */
@NoArgsConstructor
public class InsertOrUpdateBatchMethod extends AbstractMethod {

    /**
     * 更新字段判断
     */
    private static final Predicate<TableFieldInfo> UPDATE_PREDICATE = tableFieldInfo -> {
        Field field = tableFieldInfo.getField();
        OnDuplicateUpdate onDuplicateUpdate = field.getAnnotation(OnDuplicateUpdate.class);
        if (onDuplicateUpdate == null) {
            return false;
        }
        return onDuplicateUpdate.value();
    };

    private static final String ON_DUPLICATE_KEY_UPDATE_PREFIX_SQL = "\nON DUPLICATE KEY UPDATE\n";
    private static final String COLUMN_VALUES_SQL = "%s = values(%s)";
    private static final String BATCH_INSERT_SQL = "<script>\nINSERT INTO %s %s VALUES %s %s</script>";

    private static final String METHOD_NAME = "insertOrUpdateBatch";

    @SuppressWarnings("Duplicates")
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        //插入的列名
        String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(false) +
                this.filterTableFieldInfo(fieldList, null, TableFieldInfo::getInsertSqlColumn, EMPTY);
        if (StrUtil.isBlank(insertSqlColumn)) {
            insertSqlColumn = StringPool.SPACE;
        }
        String columnScript = LEFT_BRACKET + insertSqlColumn.substring(0, Math.max(insertSqlColumn.length(), 1) - 1) + RIGHT_BRACKET;
        //插入的列值
        String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(ENTITY_DOT, false) +
                this.filterTableFieldInfo(fieldList, null, i -> i.getInsertSqlProperty(ENTITY_DOT), EMPTY);
        insertSqlProperty = LEFT_BRACKET + insertSqlProperty.substring(0, Math.max(insertSqlProperty.length(), 1) - 1) + RIGHT_BRACKET;
        String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, "list", null, ENTITY, COMMA);
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (tableInfo.havePK()) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(METHOD_NAME, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        //更新字段内容
        String updateColumnValues = this.filterTableFieldInfo(fieldList, UPDATE_PREDICATE, tableFieldInfo -> String.format(COLUMN_VALUES_SQL, tableFieldInfo.getColumn(), tableFieldInfo.getColumn()), StringPool.COMMA);
        String updateSql;
        if (StrUtil.isNotBlank(updateColumnValues)) {
            updateSql = ON_DUPLICATE_KEY_UPDATE_PREFIX_SQL + updateColumnValues;
        } else {
            updateSql = StringPool.EMPTY;
        }

        String sql = String.format(BATCH_INSERT_SQL, tableInfo.getTableName(), columnScript, valuesScript, updateSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return this.addInsertMappedStatement(mapperClass, modelClass, METHOD_NAME, sqlSource, keyGenerator, keyProperty, keyColumn);
    }


    @Override
    public String getMethod(SqlMethod sqlMethod) {
        // 自定义 mapper 方法名
        return METHOD_NAME;
    }
}
