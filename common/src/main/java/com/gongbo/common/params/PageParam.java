package com.gongbo.common.params;


import com.gongbo.common.constant.ParamNames;

/**
 * 分页查询参数
 */
public interface PageParam extends BaseParam {

    default Integer getPageSize() {
        return (Integer) getParamValue(ParamNames.PAGE_SIZE, 20);
    }

    default Integer getPage() {
        return (Integer) getParamValue(ParamNames.PAGE, 1);
    }

    default void setPageSize(Integer value) {
        setParamValue(ParamNames.PAGE_SIZE, value);
    }

    default void setPage(Integer value) {
        setParamValue(ParamNames.PAGE, value);
    }
}