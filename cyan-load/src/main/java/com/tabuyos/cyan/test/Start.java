package com.tabuyos.cyan.test;

import com.tabuyos.cyan.config.AutowireJar;
import com.tabuyos.cyan.util.ScanUtil;

import java.util.*;

/**
 * @Author Tabuyos
 * @Time 2020/4/3 16:20
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class Start {
    public static void main(String[] args) {
        List<String> pathList = new ArrayList<>(Arrays.asList(args));
//        pathList.add("F:/Temp/asm/fastjson-1.2.68.jar");
        ScanUtil scanUtil = new ScanUtil(pathList);
        scanUtil.parseJar();
        scanUtil.parseClass();

        Scanner scanner = new Scanner(System.in);
        System.out.println("input object or class: ");
        String key = scanner.nextLine();
        Map<String, String> loadedMap = AutowireJar.getLoadedMap();
        Map<String, List<Map<String, String>>> classMap = AutowireJar.getClassMap();
        Map<String, String> classInfoMap = AutowireJar.getClassInfoMap();

        while (!key.equals("exit")) {
            try {
                // add object or class to loadedMap
                if (loadedMap.containsKey(key)) {
                    getFieldOrMethod(scanner, loadedMap, classMap, key);
                } else if (classInfoMap.containsKey(key)) {
                    // simulate import
                    System.out.println(classInfoMap.get(key));
                    System.out.println("input name: ");
                    String name = scanner.nextLine();
                    loadedMap.put(name, key);
                    getFieldOrMethod(scanner, loadedMap, classMap, name);
                } else {
                    System.out.println("Not Found the object or class, try again please.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("input object or class: ");
                key = scanner.nextLine();
            }
        }
    }

    private static void getFieldOrMethod(Scanner scanner, Map<String, String> loadedMap, Map<String, List<Map<String, String>>> classMap, String name) {
        String className = loadedMap.get(name);
        System.out.println(name + ".");
        String fieldOrMethodName = scanner.nextLine();
        for (Map<String, String> map : classMap.get(className)) {
            for (String keyString : map.keySet()) {
                if (keyString.toLowerCase().contains(fieldOrMethodName.toLowerCase())) {
                    getTips(map, keyString);
                } else if (fieldOrMethodName.equals("")) {
                    getTips(map, keyString);
                }
            }
        }
    }

    private static void getTips(Map<String, String> map, String keyString) {
        if (map.get(keyString).contains("public")) {
            System.out.println(keyString);
        }
        for (String substring : map.get(keyString).split("===")) {
            if (substring.contains("public")) {
                System.out.println("\t" + substring);
            }
        }
    }
}
