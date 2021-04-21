package com.gongbo.common.params;


import com.gongbo.common.constant.ParamNames;
import com.gongbo.common.validate.custom.annotation.RangeEnd;
import com.gongbo.common.validate.custom.annotation.RangeStart;

import java.time.LocalDate;

/**
 * 日期范围参数
 */
public interface DateRangeParam extends BaseParam {

    @RangeStart(rangeGroup = "date_range", message = "开始日期不能大于结束日期")
    default LocalDate getStartDate() {
        return (LocalDate) getParamValue(ParamNames.START_DATE);
    }

    @RangeEnd(rangeGroup = "date_range", message = "开始日期不能大于结束日期")
    default LocalDate getEndDate() {
        return (LocalDate) getParamValue(ParamNames.END_DATE);
    }

    default void setStartDate(LocalDate date) {
        setParamValue(ParamNames.START_DATE, date);
    }

    default void setEndDate(LocalDate date) {
        setParamValue(ParamNames.END_DATE, date);
    }

}
