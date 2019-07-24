package com.lephix.easy.utils;

import java.util.HashMap;
import java.util.Map;

public class MapHelper<T> {

    private Map<T, Object> map;

    public static <T> MapHelper<T> getInstance() {
        return new MapHelper<>();
    }

    public static <T> MapHelper<T> getInstance(int size) {
        return new MapHelper<>();
    }

    private MapHelper() {
        map = new HashMap<>();
    }

    private MapHelper(int size) {
        map = new HashMap<>(size);
    }

    private MapHelper(Map<T, Object> map) {
        this.map = map;
    }

    public MapHelper<T> add(T key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<T, Object> build() {
        return map;
    }
}
