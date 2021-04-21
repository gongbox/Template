package com.gongbo.common.params.impl;


import com.gongbo.common.params.BaseParam;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class BaseParamImpl implements BaseParam {

    protected Map<String, Object> paramMap;

    @Override
    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    @Override
    public void setParamValue(String key, Object value) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.put(key, value);
    }

    @Override
    public Object getParamValue(String key) {
        if (paramMap == null) {
            return null;
        }

        return paramMap.get(key);
    }

    @Override
    public Object getParamValue(String key, Object defaultValue) {
        if (paramMap == null) {
            return null;
        }

        return paramMap.getOrDefault(key, defaultValue);
    }
}
