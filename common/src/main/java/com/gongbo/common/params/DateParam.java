package com.gongbo.common.params;


import com.gongbo.common.constant.ParamNames;

import java.time.LocalDate;

/**
 * 日期参数
 */
public interface DateParam extends BaseParam {

    default LocalDate getDate() {
        return (LocalDate) getParamValue(ParamNames.DATE);
    }

    default void setDate(LocalDate value) {
        setParamValue(ParamNames.DATE, value);
    }

}
