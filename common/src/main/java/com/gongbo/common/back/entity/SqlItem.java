package com.gongbo.common.back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SqlItem {
    private String sql;
    private Object[] extra;

    public static List<SqlItem> wrap(Collection<String> collection, Object[] extra) {
        return collection.stream().map(s -> new SqlItem(s, extra == null ? new Object[0] : extra)).collect(Collectors.toList());
    }

    public static List<SqlItem> wrap(Collection<String> collection) {
        return wrap(collection, new Object[0]);
    }

    public static List<String> unwrap(Collection<SqlItem> collection) {
        return collection.stream().map(SqlItem::getSql).collect(Collectors.toList());
    }
}
