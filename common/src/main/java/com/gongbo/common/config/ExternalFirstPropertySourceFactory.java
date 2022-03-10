package com.gongbo.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

/**
 * 优先使用外部配置文件加载的PropertySourceFactory
 */
@Slf4j
public class ExternalFirstPropertySourceFactory extends DefaultPropertySourceFactory {

    private static final String[] EXTERNAL_PATH = {"./config/", "./"};

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        //尝试获取外部配置文件
        if (resource.getResource() instanceof ClassPathResource) {
            String path = ((ClassPathResource) resource.getResource()).getPath();

            for (String externalPath : EXTERNAL_PATH) {
                FileUrlResource fileUrlResource = new FileUrlResource(externalPath + path);
                if (fileUrlResource.exists()) {
                    log.info("user external config file：{}", fileUrlResource.getFile().getPath());
                    return (name != null ? new ResourcePropertySource(name, fileUrlResource) : new ResourcePropertySource(fileUrlResource));
                }
            }
        }
        return super.createPropertySource(name, resource);
    }
}
