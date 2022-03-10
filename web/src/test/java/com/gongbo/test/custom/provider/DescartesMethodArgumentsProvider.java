//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gongbo.test.custom.provider;

import com.gongbo.common.utils.CollectionUtils;
import com.gongbo.test.custom.annotations.DescartesMethodSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DescartesMethodArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<DescartesMethodSource> {
    private MethodSource[] methodSources;

    @Override
    public void accept(DescartesMethodSource methodParamSource) {
        this.methodSources = methodParamSource.value();
    }

    public Stream<Arguments> provideArguments(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();

        Parameter[] parameters = testMethod.getParameters();

        List<Object>[] arguments = new List[parameters.length];

        for (int i = 0; i < methodSources.length; i++) {
            MethodSource methodSource = methodSources[i];
            String[] methodNames = methodSource.value();
            arguments[i] = provideArguments(context, methodNames);
        }

        List<Object[]> result = CollectionUtils.descartes(arguments);

        return result.stream()
                .map(Arguments::of);
    }

    public List<Object> provideArguments(ExtensionContext context, String[] methodNames) {
        Object testInstance = context.getTestInstance().orElse(null);
        return Arrays.stream(methodNames)
                .map((factoryMethodName) -> this.getMethod(context, factoryMethodName))
                .map((method) -> ReflectionUtils.invokeMethod(method, testInstance, new Object[0]))
                .flatMap(org.junit.platform.commons.util.CollectionUtils::toStream).collect(Collectors.toList());
    }

    private Method getMethod(ExtensionContext context, String factoryMethodName) {
        if (StringUtils.isNotBlank(factoryMethodName)) {
            return factoryMethodName.contains("#") ? this.getMethodByFullyQualifiedName(factoryMethodName) : this.getMethod(context.getRequiredTestClass(), factoryMethodName);
        } else {
            return this.getMethod(context.getRequiredTestClass(), context.getRequiredTestMethod().getName());
        }
    }

    private Method getMethodByFullyQualifiedName(String fullyQualifiedMethodName) {
        String[] methodParts = ReflectionUtils.parseFullyQualifiedMethodName(fullyQualifiedMethodName);
        String className = methodParts[0];
        String methodName = methodParts[1];
        String methodParameters = methodParts[2];
        Preconditions.condition(StringUtils.isBlank(methodParameters),
                () -> String.format("factory method [%s] must not declare formal parameters", fullyQualifiedMethodName));
        return this.getMethod(this.loadRequiredClass(className), methodName);
    }

    private Class<?> loadRequiredClass(String className) {
        return ReflectionUtils.tryToLoadClass(className)
                .getOrThrow((cause) -> new JUnitException(String.format("Could not load class [%s]", className), cause));
    }

    private Method getMethod(Class<?> clazz, String methodName) {
        return ReflectionUtils.findMethod(clazz, methodName, new Class[0])
                .orElseThrow(() -> new JUnitException(String.format("Could not find factory method [%s] in class [%s]", methodName, clazz.getName())));
    }

}
