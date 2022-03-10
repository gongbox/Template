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
package com.gongbo.common.batch.wrapper;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * @author miemie
 * @since 2018-12-12
 */
public interface Insert<Children, R> extends Serializable {

    /**
     * 设置更新字段的值
     *
     * @param column
     * @param val
     * @return
     */
    default Children onDuplicateKeyUpdate(R column, Object val) {
        return onDuplicateKeyUpdate(true, column, val);
    }

    /**
     * 条件为真时，设置更新字段的值
     *
     * @param condition
     * @param column
     * @param val
     * @return
     */
    Children onDuplicateKeyUpdate(boolean condition, R column, Object val);

    /**
     * 设置更新的字段
     *
     * @param column
     * @return
     */
    default Children onDuplicateKeyUpdateValues(R column) {
        return onDuplicateKeyUpdateValues(true, column);
    }

    /**
     * 条件为真时，设置更新的字段
     *
     * @param condition
     * @param column
     * @return
     */
    Children onDuplicateKeyUpdateValues(boolean condition, R column);

    /**
     * 设置真实的实体类，CommonValDynamicService使用的需要
     * @param entityClass
     * @return
     */
    Children setRealEntityClass(Class entityClass);

    /**
     * 条件为真时，设置更新所有字段
     *
     * @param condition
     * @param predicate 过滤字段
     * @return
     */
    Children onDuplicateKeyUpdateValues(boolean condition, Predicate<TableFieldInfo> predicate);

    /**
     * 设置更新所有字段
     *
     * @param predicate 过滤字段
     * @return
     */
    default Children onDuplicateKeyUpdateValues(Predicate<TableFieldInfo> predicate) {
        return onDuplicateKeyUpdateValues(true, predicate);
    }

    /**
     * 设置更新所有字段
     *
     * @return
     */
    default Children onDuplicateKeyUpdateValues() {
        return onDuplicateKeyUpdateValues(e -> true);
    }

    /**
     * 设置更新内容
     *
     * @param sql
     * @return
     */
    default Children onDuplicateKeyUpdateSql(String sql) {
        return onDuplicateKeyUpdateSql(true, sql);
    }

    /**
     * 条件为真时，设置更新内容
     *
     * @param condition
     * @param sql
     * @return
     */
    Children onDuplicateKeyUpdateSql(boolean condition, String sql);

    /**
     * 或者 on duplicate key update 语句后面的sql
     */
    String getSqlOnDuplicateKeyUpdate();
}
