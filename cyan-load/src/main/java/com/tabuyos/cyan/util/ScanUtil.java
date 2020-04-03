package com.tabuyos.cyan.util;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * @Author Tabuyos
 * @Time 2020/4/3 15:51
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class ScanUtil {
    private final String path;
    private final Map<String, Map<String, String>> map = new HashMap<>();

    public ScanUtil(String path) {
        this.path = path;
    }

    public void parseJar() {
        try {
            String jarName = FileUtil.getFileName(path, Constant.SUFFIX_JAR);
            JarFile jarFile = new JarFile(path);
            System.out.println(jarName);
//            System.out.println("==============");
//            String[] strings = path.replace(File.separator, "==").split("==");
//            System.out.println(strings[strings.length-1]);
//            System.out.println(Arrays.toString(strings));
            Enumeration<JarEntry> enumeration = jarFile.entries();
            Map<String, String> classMap = new HashMap<>();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    String className = FileUtil.getFileName(name, Constant.SUFFIX_CLASS);
                    String classPath = FileUtil.getClassPackage(name);
                    classMap.put(className, classPath);
                    System.out.println("name = " + className);
                    System.out.println("package = " + classPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
