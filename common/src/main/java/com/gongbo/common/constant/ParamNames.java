package com.gongbo.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 请求参数名常量
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamNames {

    /**
     * 开始日期（格式：yyyy-MM-dd）
     */
    public static final String START_DATE = "startDate";
    /**
     * 结束日期（格式：yyyy-MM-dd）
     */
    public static final String END_DATE = "endDate";


    /**
     * 开始日期（格式：yyyy-MM-dd hh:mm:ss）
     */
    public static final String START_DATE_TIME = "startDateTime";
    /**
     * 结束日期（格式：yyyy-MM-dd hh:mm:ss）
     */
    public static final String END_DATE_TIME = "endDateTime";

    /**
     * 日期（格式：yyyy-MM-dd）
     */
    public static final String DATE = "date";

    /**
     * 月份
     */
    public static final String MONTH = "month";

    /**
     * 当前页数
     */
    public static final String PAGE = "page";

    /**
     * 每页条数
     */
    public static final String PAGE_SIZE = "pageSize";

}
