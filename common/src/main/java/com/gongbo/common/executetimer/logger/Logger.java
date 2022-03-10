package com.gongbo.common.executetimer.logger;

public interface Logger {

    /**
     * 运行时间日志
     */
    void info(String name, long time);

    /**
     * 超时日志
     */
    void warn(String name, long time);
}
