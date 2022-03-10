package com.gongbo.common.batch.wrapper;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@SuppressWarnings("serial")
public class LambdaInsertWrapper<T> extends AbstractLambdaWrapper<T, LambdaInsertWrapper<T>>
        implements Insert<LambdaInsertWrapper<T>, SFunction<T, ?>> {

    /**
     * SQL 更新字段内容，例如：name='1', age=2
     */
    private final List<String> sqlOnDuplicateUpdate;

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate()
     */
    public LambdaInsertWrapper() {
        // 如果无参构造函数，请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        this((T) null);
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(entity)
     */
    public LambdaInsertWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
        this.sqlOnDuplicateUpdate = new ArrayList<>();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(entity)
     */
    public LambdaInsertWrapper(Class<T> entityClass) {
        super.setEntityClass(entityClass);
        super.initNeed();
        this.sqlOnDuplicateUpdate = new ArrayList<>();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(...)
     */
    LambdaInsertWrapper(T entity, Class<T> entityClass, List<String> sqlOnDuplicateUpdate, AtomicInteger paramNameSeq,
                        Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                        SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.sqlOnDuplicateUpdate = sqlOnDuplicateUpdate;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    @Override
    public LambdaInsertWrapper<T> setRealEntityClass(Class entityClass) {
        return setEntityClass(entityClass);
    }

    @Override
    public LambdaInsertWrapper<T> onDuplicateKeyUpdate(boolean condition, SFunction<T, ?> column, Object val) {
        if (condition) {
            sqlOnDuplicateUpdate.add(String.format("%s=%s", columnToString(column), formatSql("{0}", val)));
        }
        return typedThis;
    }

    @Override
    public LambdaInsertWrapper<T> onDuplicateKeyUpdateValues(boolean condition, SFunction<T, ?> column) {
        if (condition) {
            String columnToString = columnToString(column);
            sqlOnDuplicateUpdate.add(String.format("%s=VALUES(%s)", columnToString, columnToString));
        }
        return typedThis;
    }

    @Override
    public LambdaInsertWrapper<T> onDuplicateKeyUpdateValues(boolean condition, Predicate<TableFieldInfo> predicate) {
        if (condition) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
            List<TableFieldInfo> fieldList = tableInfo.getFieldList();
            for (TableFieldInfo tableFieldInfo : fieldList) {
                if (!predicate.test(tableFieldInfo)) continue;
                String column = tableFieldInfo.getColumn();
                sqlOnDuplicateUpdate.add(String.format("%s=VALUES(%s)", column, column));
            }
        }
        return typedThis;
    }

    @Override
    public LambdaInsertWrapper<T> onDuplicateKeyUpdateSql(boolean condition, String sql) {
        if (condition && StringUtils.isNotBlank(sql)) {
            sqlOnDuplicateUpdate.add(sql);
        }
        return typedThis;
    }

    @Override
    public String getSqlOnDuplicateKeyUpdate() {
        if (CollectionUtils.isEmpty(sqlOnDuplicateUpdate)) {
            return null;
        }
        return String.join(StringPool.COMMA, sqlOnDuplicateUpdate);
    }

    @Override
    protected LambdaInsertWrapper<T> instance() {
        return new LambdaInsertWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlOnDuplicateUpdate.clear();
    }
}
