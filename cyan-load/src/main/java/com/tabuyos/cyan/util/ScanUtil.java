package com.tabuyos.cyan.util;

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
    private Map<String, List<String>> names = new HashMap<>();


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
                List<String> classNameList = new ArrayList<>();
                String jarName = FileUtil.getFileName(path, Constant.SUFFIX_JAR);
                if (AutowireJar.getJarInfoMap().containsKey(jarName)) {
                    continue;
                }
                JarFile jarFile = new JarFile(path);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                AutowireJar.getJarInfoMap().put(jarName, FileUtil.normalize(path));
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = enumeration.nextElement();
                    String name = jarEntry.getName();
                    if (name.endsWith(Constant.SUFFIX_CLASS)) {
                        classNameList.add(name);
                        String className = FileUtil.getFileName(name, Constant.SUFFIX_CLASS);
                        String classPath = FileUtil.getClassPackage(name);
                        AutowireJar.getClassInfoMap().put(className, classPath);
                    }
                }
                names.put(path, classNameList);
                jarFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // use asm parse class

    /**
     * arg0: pathName, arg1-n: classList
     * @param classPaths pathName and classList
     */
    public void parseClass(String... classPaths) {
        if (classPaths.length > 0) {
            List<String> classNameList = new ArrayList<>(Arrays.asList(classPaths).subList(1, classPaths.length));
            names.put(FileUtil.getFileName(classPaths[0], Constant.SUFFIX_CLASS), classNameList);
        }
        for (String path : paths) {
            try {
                File file = new File(path);
                String jarName = FileUtil.getFileName(path, Constant.SUFFIX_JAR);
                if (AutowireJar.getJarMap().containsKey(jarName)) {
                    continue;
                }
                URL url = file.toURI().toURL();
                ClassLoader loader = new URLClassLoader(new URL[]{url});
                for (String name : names.get(path)) {
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

                    for (FieldNode fieldNode : fields) {
                        String fieldString = ModifierUtil.getModifier(fieldNode.access) + Constant.SPACE +
                                Type.getType(fieldNode.desc).getClassName() + Constant.SPACE +
                                fieldNode.name + Constant.SPACE;
                        fieldMap.put(fieldNode.name, fieldString.trim());
                    }

                    // handler method
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
                        methodMap.put(methodName, methodString.toString().trim());
                    }
                    list.add(fieldMap);
                    list.add(methodMap);
                    AutowireJar.getClassMap().put(className + Constant.DELIMITER + classPackage, list);
                }
                AutowireJar.getJarMap().put(jarName, AutowireJar.getClassMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
