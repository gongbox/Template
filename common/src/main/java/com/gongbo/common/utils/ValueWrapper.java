package com.gongbo.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ValueWrapper<T> {
    private T value;

    public static <T> List<ValueWrapper<T>> wraps(Collection<T> data) {
        return data.stream().map(ValueWrapper::of).collect(Collectors.toList());
    }

}
