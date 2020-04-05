package com.tabuyos.cyan.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Tabuyos
 * @Time 2020/4/4 19:06
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class AutowireJar {

    private static Map<String, String> jarInfoMap = new HashMap<>();
    private static Map<String, Map<String, List<Map<String, String>>>> jarMap = new HashMap<>();
    private static Map<String, String> classInfoMap = new HashMap<>();
    private static Map<String, List<Map<String, String>>> classMap = new HashMap<>();
    private static Map<String, String> loadedMap = new HashMap<>();

    public static Map<String, String> getJarInfoMap() {
        return jarInfoMap;
    }

    public static Map<String, Map<String, List<Map<String, String>>>> getJarMap() {
        return jarMap;
    }

    public static Map<String, String> getClassInfoMap() {
        return classInfoMap;
    }

    public static Map<String, List<Map<String, String>>> getClassMap() {
        return classMap;
    }

    public static Map<String, String> getLoadedMap() {
        return loadedMap;
    }
}
