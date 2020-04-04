package com.tabuyos.cyan.util;

import com.tabuyos.cyan.config.AutowireJar;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
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
    private final String[] paths;
    private final List<String> names = new ArrayList<>();
//    private final Map<String, Map<String, String>> map = new HashMap<>();

    public ScanUtil(String[] paths) {
        this.paths = paths;
    }

    public void parseJar() {
        for (String path : paths) {
            try {
                String jarName = FileUtil.getFileName(path, Constant.SUFFIX_JAR);
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

    public void parseASM() {
        for (String path : paths) {
            try {
                File file = new File(path);//jar包的路径
                URL url = file.toURI().toURL();
                ClassLoader loader = new URLClassLoader(new URL[]{url});//创建类加载器
                for (String name : names) {
                    ClassReader reader = new ClassReader(Objects.requireNonNull(loader.getResourceAsStream(name)));
//                ClassReader reader = new ClassReader(new FileInputStream(new File("F:/Temp/asm/asm.class")));
                    ClassNode cn = new ClassNode();
                    reader.accept(cn, 0);
                    String superName = cn.superName;
                    List<String> interfaces = cn.interfaces;

                    // handler class
                    System.out.println("====================Tabuyos-Class====================");
                    System.out.println(cn.name.replace("/", "."));
                    System.out.println("====================Tabuyos-Class====================");

                    // handler superclass
                    System.out.println("====================Tabuyos-SuperClass====================");
                    System.out.println("Super Class: " + superName.replace("/", "."));
                    System.out.println("====================Tabuyos-SuperClass====================");

                    // handler interface
                    System.out.println("====================Tabuyos-Interface====================");
                    for (String anInterface : interfaces) {
                        System.out.println("Interface Class: " + anInterface.replace("/", "."));
                    }
                    System.out.println("====================Tabuyos-Interface====================");

                    List<FieldNode> fields = cn.fields;
                    List<MethodNode> methodNodes = cn.methods;

                    // handler field
                    System.out.println("====================Tabuyos-Field====================");
                    for (FieldNode fieldNode : fields) {
                        System.out.println(ModifierUtil.getModifier(fieldNode.access) + " " + Type.getType(fieldNode.desc).getClassName() + " " + fieldNode.name);
                    }
                    System.out.println("====================Tabuyos-Field====================");

                    // handler method
                    System.out.println("====================Tabuyos-Method====================");
                    for (MethodNode methodNode : methodNodes) {

                        System.out.print(ModifierUtil.getModifier(methodNode.access) + " " + Type.getReturnType(methodNode.desc).getClassName() + " " + methodNode.name + "(");
                        int argCount = 0;
                        for (Type argumentType : Type.getArgumentTypes(methodNode.desc)) {
                            System.out.print(argumentType.getClassName() + " arg" + argCount + ", ");
                            argCount++;
                        }
                        System.out.println(")");
//                InsnList insnList = methodNode.instructions;
//                for (int i = 0; i < insnList.size(); i++) {
//                    if (insnList.get(i) instanceof MethodInsnNode) {
//                        MethodInsnNode methodInsnNode = (MethodInsnNode)insnList.get(i);
//                        System.out.println("\t" + "using method name: " + methodInsnNode.name + ". using method type: " + methodInsnNode.desc);
//                        System.out.println(Type.getType(methodInsnNode.desc).getInternalName());
//                    }
//                }
                    }
                    System.out.println("====================Tabuyos-Method====================");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public Map<String, Map<String, String>> getMap() {
//        return map;
//    }
}
