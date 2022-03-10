package com.gongbo.common.executetimer.config;

import com.gongbo.common.executetimer.aspect.ExecuteTimerAdvise;
import com.gongbo.common.executetimer.logger.Logger;
import com.gongbo.common.executetimer.logger.impl.LoggerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class ExecuteTimerConfig {

    @Bean
    public Logger logger() {
        return new LoggerImpl();
    }

    @Bean
    public ExecuteTimerAdvise executeTimerAdvise() {
        return new ExecuteTimerAdvise();
    }
}
