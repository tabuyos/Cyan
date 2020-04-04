package com.tabuyos.cyan.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tabuyos
 * @Time 2020/4/4 19:06
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class AutowireJar {
    private static final Map<String, Object> map = new HashMap<>();
    private static final Map<String, Object> jarInfoMap = new HashMap<>();
    private static final Map<String, Object> jarMap = new HashMap<>();
    private static final Map<String, Object> classInfoMap = new HashMap<>();
    private static final Map<String, Object> classMap = new HashMap<>();
    private static final Map<String, Object> loadedMap = new HashMap<>();

    public static Map<String, Object> getMap() {
        return map;
    }

    public static Map<String, Object> getJarInfoMap() {
        return jarInfoMap;
    }

    public static Map<String, Object> getJarMap() {
        return jarMap;
    }

    public static Map<String, Object> getClassInfoMap() {
        return classInfoMap;
    }

    public static Map<String, Object> getClassMap() {
        return classMap;
    }

    public static Map<String, Object> getLoadedMap() {
        return loadedMap;
    }
}
