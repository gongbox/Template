package com.gongbo.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Equals {
    /**
     * 比较Page对象是否相等
     */
    public static <T> boolean equals(Page<T> page1, Page<T> page2) {
        if (page1.getSize() != page2.getSize()
                || page1.getTotal() != page2.getTotal()
                || page1.getCurrent() != page2.getCurrent()
                || page1.getSize() != page2.getSize()) {
            return false;
        }
        return page1.getRecords().equals(page2.getRecords());
    }

    /**
     * 比较集合师傅相等
     */
    public static boolean equals(Collection<?> c1, Collection<?> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        } else {
            if (c1.size() != c2.size()) {
                return false;
            }
            for (Object o : c1) {
                if (!c2.contains(o)) {
                    return false;
                }
            }
        }
        return true;
    }
}
