package com.gongbo.common.back.core;


import com.gongbo.common.back.entity.SqlItem;

import java.util.Collection;

public interface BackConsumer {

    void consume(Integer delayMs, Collection<SqlItem> sqls);
}
