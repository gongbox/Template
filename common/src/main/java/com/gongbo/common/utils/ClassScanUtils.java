package com.gongbo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 扫描指定包下的类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassScanUtils {
    /**
     * 扫描包下的所有类
     *
     * @param basePackages 包路径集合
     * @param predicate    过滤条件
     * @return
     */
    public static List<Class<?>> scan(String[] basePackages, Predicate<Class<?>> predicate) {
        return Arrays.stream(basePackages).flatMap(s -> scan(s, predicate).stream()).collect(Collectors.toList());
    }

    /**
     * 扫描包下的所有类
     *
     * @param basePackage 包路径
     * @param predicate   过滤条件
     * @return
     */
    public static List<Class<?>> scan(String basePackage, Predicate<Class<?>> predicate) {
        List<Class<?>> classes = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(basePackage) + ".class";
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                //过滤
                if (predicate.test(clazz)) {
                    classes.add(clazz);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
