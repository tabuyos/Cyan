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

        Map<String, String> loadedMap = AutowireJar.getLoadedMap();
        Map<String, List<Map<String, String>>> classMap = AutowireJar.getClassMap();
        Map<String, String> classInfoMap = AutowireJar.getClassInfoMap();
        Map<String, String> jarInfoMap = AutowireJar.getJarInfoMap();

//        pathList.add("F:/Temp/asm/fastjson-1.2.68.jar");

        ScanUtil scanUtil = new ScanUtil(pathList);
        scanUtil.parseJar();
        scanUtil.parseClass();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Show jar information, type 0");
        System.out.println("Show class information, type 1");
        System.out.println("Input object or class, type 2");
        System.out.print("Type 0/1/2? ");
        String flag = scanner.nextLine();
        while (!flag.equals("2")) {
            switch (flag) {
                case "0":
                    showJarInfo(jarInfoMap);
                    break;
                case "1":
                    showClassInfo(classInfoMap);
                    break;
                default:
                    System.out.println("Type error. Try again.");
                    break;
            }
            System.out.print("Type 0/1/2? ");
            flag = scanner.nextLine();
        }
        System.out.print("Input object or class name: ");
        String objectOrClassName = scanner.nextLine();

        while (!objectOrClassName.equals("exit")) {
            try {
                // add object or class to loadedMap
                if (loadedMap.containsKey(objectOrClassName)) {
                    getFieldOrMethod(scanner, loadedMap, classMap, objectOrClassName);
                } else if (classInfoMap.containsKey(objectOrClassName)) {
                    // simulate import
                    System.out.println(classInfoMap.get(objectOrClassName));
                    System.out.print("Input object name: ");
                    String name = scanner.nextLine();
                    loadedMap.put(name, objectOrClassName);
                    getFieldOrMethod(scanner, loadedMap, classMap, name);
                } else {
                    System.out.println("Not Found the object or class, try again please.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.print("Input object or class name: ");
                objectOrClassName = scanner.nextLine();
            }
        }
    }

    private static void getFieldOrMethod(Scanner scanner, Map<String, String> loadedMap, Map<String, List<Map<String, String>>> classMap, String name) {
        String className = loadedMap.get(name);
        System.out.print(name + ".");
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

    private static void showJarInfo(Map<String, String> jarInfoMap) {
        for (String key : jarInfoMap.keySet()) {
            System.out.println(key + "\n\t" + jarInfoMap.get(key));
        }
    }

    private static void showClassInfo(Map<String, String> classInfoMap) {
        for (String key : classInfoMap.keySet()) {
            System.out.println(key + "\n\t" + classInfoMap.get(key));
        }
    }
}
