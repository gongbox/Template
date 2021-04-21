package com.gongbo.common.params;

import java.util.Map;

public interface BaseParam {

    Map<String, Object> getParamMap();

    void setParamValue(String key, Object value);

    Object getParamValue(String key);

    Object getParamValue(String key, Object defaultValue);
}
