package com.tabuyos.cyan.util;

import java.util.HashMap;

/**
 * @Author Tabuyos
 * @Time 2020/4/4 21:16
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class RepeatHashMap extends HashMap<String, String> {
    @Override
    public String put(String key, String value) {
        if (containsKey(key)) {
            value = get(key) + "===" + value;
        }
        return super.put(key, value);
    }
}
