package com.lephix.easy.utils;

import java.util.HashMap;
import java.util.Map;

public class MapHelper<K, V> {

    private Map<K, V> map;

    public static <K, V> MapHelper<K, V> getInstance() {
        return new MapHelper<>();
    }

    public static <K, V> MapHelper<K, V> getInstance(int size) {
        return new MapHelper<>(size);
    }

    public static <K, V> MapHelper<K, V> getInstance(Map<K, V> map) {
        return new MapHelper<>(map);
    }


    private MapHelper() {
        map = new HashMap<>();
    }

    private MapHelper(int size) {
        map = new HashMap<>(size);
    }

    private MapHelper(Map<K, V> map) {
        this.map = map;
    }

    public MapHelper<K, V> add(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
