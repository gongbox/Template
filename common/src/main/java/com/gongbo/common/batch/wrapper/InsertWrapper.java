package com.gongbo.common.batch.wrapper;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@SuppressWarnings("serial")
public class InsertWrapper<T> extends AbstractWrapper<T, String, InsertWrapper<T>>
        implements Insert<InsertWrapper<T>, String> {

    /**
     * SQL 更新字段内容，例如：name='1', age=2
     */
    private final List<String> sqlOnDuplicateUpdate;

    public InsertWrapper() {
        // 如果无参构造函数，请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        this(null);
    }

    public InsertWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
        this.sqlOnDuplicateUpdate = new ArrayList<>();
    }

    private InsertWrapper(T entity, List<String> sqlOnDuplicateUpdate, AtomicInteger paramNameSeq,
                          Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                          SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        this.sqlOnDuplicateUpdate = sqlOnDuplicateUpdate;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    @Override
    public InsertWrapper<T> setRealEntityClass(Class entityClass) {
        return setEntityClass(entityClass);
    }

    @Override
    public String getSqlOnDuplicateKeyUpdate() {
        if (CollectionUtils.isEmpty(sqlOnDuplicateUpdate)) {
            return null;
        }
        return String.join(StringPool.COMMA, sqlOnDuplicateUpdate);
    }

    @Override
    public InsertWrapper<T> onDuplicateKeyUpdate(boolean condition, String column, Object val) {
        if (condition) {
            sqlOnDuplicateUpdate.add(String.format("%s=%s", column, formatSql("{0}", val)));
        }
        return typedThis;
    }

    @Override
    public InsertWrapper<T> onDuplicateKeyUpdateValues(boolean condition, String column) {
        if (condition) {
            sqlOnDuplicateUpdate.add(String.format("%s=VALUES(%s)", column, column));
        }
        return typedThis;
    }

    @Override
    public InsertWrapper<T> onDuplicateKeyUpdateValues(boolean condition, Predicate<TableFieldInfo> predicate) {
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
    public InsertWrapper<T> onDuplicateKeyUpdateSql(boolean condition, String sql) {
        if (condition && StringUtils.isNotBlank(sql)) {
            sqlOnDuplicateUpdate.add(sql);
        }
        return typedThis;
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    public LambdaInsertWrapper<T> lambda() {
        return new LambdaInsertWrapper<>(getEntity(), getEntityClass(), sqlOnDuplicateUpdate, paramNameSeq, paramNameValuePairs,
                expression, lastSql, sqlComment, sqlFirst);
    }

    @Override
    protected InsertWrapper<T> instance() {
        return new InsertWrapper<>(getEntity(), null, paramNameSeq, paramNameValuePairs, new MergeSegments(),
                SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlOnDuplicateUpdate.clear();
    }
}
