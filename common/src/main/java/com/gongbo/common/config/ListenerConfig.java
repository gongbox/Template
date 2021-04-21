package com.gongbo.common.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.IntrospectorCleanupListener;

@Configuration
public class ListenerConfig
{
    /**
     * Spring 刷新组件防止内存泄露
     */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean()
    {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new IntrospectorCleanupListener());
        return servletListenerRegistrationBean;
    }
}
