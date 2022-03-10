package com.gongbo.common.batch.base;

public interface BaseCustomService<T> extends BaseReplaceService<T>, BaseBatchService<T>, BaseInsertOnDuplicateKeyUpdateService<T> {
}
