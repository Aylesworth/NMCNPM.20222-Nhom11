package io.github.aylesw.mch.frontend.common;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RequestBodyMap {

    public RequestBodyMap() {
        this.map = new HashMap<>();
    }

    public RequestBodyMap(Map<String, Object> map) {
        this.map = new HashMap<>(map);
    }

    private final Map<String, Object> map;

    public String toJson() {
        return Beans.GSON.toJson(map);
    }

    public RequestBodyMap put(String key, Object value) {
        map.put(key, value);
        return this;
    }
}
