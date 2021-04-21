package com.gongbo.common.params;


import com.gongbo.common.constant.ParamNames;
import com.gongbo.common.validate.custom.annotation.RangeEnd;
import com.gongbo.common.validate.custom.annotation.RangeStart;

import java.time.LocalDateTime;

/**
 * 日期范围参数
 */
public interface DateTimeRangeParam extends BaseParam {

    @RangeStart(rangeGroup = "date_time_range", message = "开始时间不能大于结束时间")
    default LocalDateTime getStartDateTime() {
        return (LocalDateTime) getParamValue(ParamNames.START_DATE_TIME);
    }

    @RangeEnd(rangeGroup = "date_time_range", message = "开始时间不能大于结束时间")
    default LocalDateTime getEndDateTime() {
        return (LocalDateTime) getParamValue(ParamNames.END_DATE_TIME);
    }

    default void setStartDateTime(LocalDateTime date) {
        setParamValue(ParamNames.START_DATE_TIME, date);
    }

    default void setEndDateTime(LocalDateTime date) {
        setParamValue(ParamNames.END_DATE_TIME, date);
    }

}
