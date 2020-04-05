package com.tabuyos.cyan.util;

import com.alibaba.fastjson.JSONObject;
import com.tabuyos.cyan.config.AutowireJar;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author Tabuyos
 * @Time 2020/4/3 15:51
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class ScanUtil {
    private List<String> paths = new ArrayList<>();
    private List<String> names = new ArrayList<>();
//    private final Map<String, Map<String, String>> map = new HashMap<>();


    public ScanUtil() {
    }

    public ScanUtil(List<String> paths) {
        this.paths = paths;
    }

    public void parseJar(String... jarPaths) {
        if (jarPaths.length > 0) {
            paths.addAll(Arrays.asList(jarPaths));
        }
        for (String path : paths) {
            try {
                String jarName = FileUtil.getFileName(path, Constant.SUFFIX_JAR);
                if (AutowireJar.getJarInfoMap().containsKey(jarName)) {
                    continue;
                }
                JarFile jarFile = new JarFile(path);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                AutowireJar.getJarInfoMap().put(jarName, FileUtil.normalize(path));
//                Map<String, String> classMap = new HashMap<>();
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = enumeration.nextElement();
                    String name = jarEntry.getName();
                    if (name.endsWith(".class")) {
                        names.add(name);
                        String className = FileUtil.getFileName(name, Constant.SUFFIX_CLASS);
                        String classPath = FileUtil.getClassPackage(name);
//                        classMap.put(className, classPath);
                        AutowireJar.getClassInfoMap().put(className, classPath);
                    }
                }
//                map.put(jarName, classMap);
                jarFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public void parseClass() {
//        try {
//            File file = new File("F:/Temp/asm/fastjson-1.2.68.jar");//jar包的路径
//            System.out.println(file.toURI());
//            URL url = file.toURI().toURL();
//            System.out.println(url);
//            ClassLoader loader = new URLClassLoader(new URL[]{url});//创建类加载器
//            Class<?> cls = loader.loadClass("com.alibaba.fastjson.JSONObject");//加载指定类，注意一定要带上类的包名
//            System.out.println(cls.getResource("").getPath());
//            System.out.println(cls.getSimpleName());
//            Method[] methods = cls.getMethods();//方法名和对应的各个参数的类型
//            System.out.println("----------------------------------------------------");
//            for (Method method : methods) {
//                System.out.println(Modifier.toString(method.getModifiers()) + " " + method.getReturnType().getName() + " " + method.getName() + " " + Arrays.toString(method.getParameters()));
////                System.out.println(method.getName());
//            }
////            Object o=method.invoke(null,"chen",Integer.valueOf(10),"0");//调用得到的上边的方法method(静态方法，第一个参数可以为null)
////            System.out.println(String.valueOf(o));//输出"000chen000","chen"字符串两边各加3个"0"字符串
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // use asm parse class
    public void parseClass(String... classPaths) {
        if (classPaths.length > 0) {
            names.addAll(Arrays.asList(classPaths));
        }
        for (String path : paths) {
            try {
                File file = new File(path);//jar包的路径
                String jarName = FileUtil.getFileName(path, Constant.SUFFIX_JAR);
                if (AutowireJar.getJarMap().containsKey(jarName)) {
                    continue;
                }
                URL url = file.toURI().toURL();
                ClassLoader loader = new URLClassLoader(new URL[]{url});//创建类加载器
                for (String name : names) {
                    InputStream inputStream = null;
                    try {
                        inputStream = Objects.requireNonNull(loader.getResourceAsStream(name));
                    } catch (NullPointerException e) {
                        try {
                            inputStream = new FileInputStream(new File(name));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                    assert inputStream != null;
                    ClassReader reader = new ClassReader(inputStream);
//                ClassReader reader = new ClassReader(new FileInputStream(new File("F:/Temp/asm/asm.class")));
                    ClassNode cn = new ClassNode();
                    reader.accept(cn, 0);
                    String superName = cn.superName;
                    List<String> interfaces = cn.interfaces;
                    List<FieldNode> fields = cn.fields;
                    List<MethodNode> methodNodes = cn.methods;
                    String className = FileUtil.getFileName(name, Constant.SUFFIX_CLASS);
                    String classPackage = cn.name.replace("/", ".");
                    Map<String, String> fieldMap = new RepeatHashMap();
                    Map<String, String> methodMap = new RepeatHashMap();
                    List<Map<String, String>> list = new ArrayList<>();

//                    // handler class
//                    AutowireJar.getClassInfoMap().put(className, classPackage);

//                    // handler superclass
//                    System.out.println("====================Tabuyos-SuperClass====================");
//                    System.out.println("Super Class: " + superName.replace("/", "."));
//                    System.out.println("====================Tabuyos-SuperClass====================");
//
//                    // handler interface
//                    System.out.println("====================Tabuyos-Interface====================");
//                    for (String anInterface : interfaces) {
//                        System.out.println("Interface Class: " + anInterface.replace("/", "."));
//                    }
//                    System.out.println("====================Tabuyos-Interface====================");

                    // handler field
//                    System.out.println("====================Tabuyos-Field====================");
                    for (FieldNode fieldNode : fields) {
                        String fieldString = ModifierUtil.getModifier(fieldNode.access) + Constant.SPACE +
                                Type.getType(fieldNode.desc).getClassName() + Constant.SPACE +
                                fieldNode.name + Constant.SPACE;
//                        System.out.println(fieldString.trim());
                        fieldMap.put(fieldNode.name, fieldString.trim());
                    }
//                    System.out.println("====================Tabuyos-Field====================");

                    // handler method
//                    System.out.println("====================Tabuyos-Method====================");
                    for (MethodNode methodNode : methodNodes) {
                        StringBuilder methodString = new StringBuilder();
                        methodString.append(ModifierUtil.getModifier(methodNode.access)).append(Constant.SPACE);
                        String methodName;
                        if (methodNode.name.equals("<init>")) {
                            methodName = className;
                        } else {
                            methodString.append(Type.getReturnType(methodNode.desc).getClassName()).append(Constant.SPACE);
                            methodName = methodNode.name;
                        }
                        methodString.append(methodName);
                        methodString.append(Constant.LEFT_PARENTHESIS);
                        int argumentCount = 0;
                        StringBuilder parameterString = new StringBuilder();
                        for (Type argumentType : Type.getArgumentTypes(methodNode.desc)) {
                            parameterString.append(argumentType.getClassName()).append(Constant.SPACE);
                            parameterString.append(Constant.ARG_FLAG).append(argumentCount);
                            parameterString.append(Constant.COMMA).append(Constant.SPACE);
                            argumentCount++;
                        }
                        if (parameterString.length() > 0) {
                            methodString.append(parameterString.substring(0, parameterString.lastIndexOf(Constant.COMMA)));
                        }
                        methodString.append(Constant.RIGHT_PARENTHESIS);
//                        System.out.println(methodString.toString().trim());
                        methodMap.put(methodName, methodString.toString().trim());
                    }
                    list.add(fieldMap);
                    list.add(methodMap);
                    AutowireJar.getClassMap().put(className, list);
//                    System.out.println("====================Tabuyos-Method====================");
                }
                AutowireJar.getJarMap().put(jarName, AutowireJar.getClassMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public Map<String, Map<String, String>> getMap() {
//        return map;
//    }
}
