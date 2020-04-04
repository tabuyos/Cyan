package com.tabuyos.cyan.test;

import com.tabuyos.cyan.config.AutowireJar;
import com.tabuyos.cyan.util.ScanUtil;

import java.util.Map;

/**
 * @Author Tabuyos
 * @Time 2020/4/3 16:20
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class Start {
    public static void main(String[] args) {
        ScanUtil scanUtil = new ScanUtil(new String[]{"F:/Temp/asm/fastjson-1.2.68.jar"});
        scanUtil.parseJar();
//        Map<String, Map<String, String>> map = scanUtil.getMap();
//        long begintime = System.currentTimeMillis();
////        System.out.println(map.get("fastjson-1.2.68").get("JSONObject"));
//        for (String s : map.keySet()) {
//            Map<String, String> m = map.get(s);
//            for (String key : m.keySet()) {
//                System.out.println(m.get(key));
//                m.get(key);
//            }
//        }
//        long endtime = System.currentTimeMillis();
//        System.out.println(endtime - begintime);
//        scanUtil.parseClass();
        scanUtil.parseASM();
        System.out.println(AutowireJar.getClassInfoMap());
    }
}
