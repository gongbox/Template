package com.gongbo.common.executetimer.logger.impl;

import com.gongbo.common.executetimer.logger.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ExecuteTimer")
public class LoggerImpl implements Logger {

    @Override
    public void info(String name, long time) {
        log.info("name: {}, execute time: {}ms", name, time);
    }

    @Override
    public void warn(String name, long time) {
        log.warn("name: {}, execute time: {}ms", name, time);
    }
}
