package com.tabuyos.cyan.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author Tabuyos
 * @Time 2020/4/3 16:32
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class FileUtil {

    public static String normalize(String originPath) {
        Path normalPath = Paths.get(originPath).normalize();
        return normalPath.toString();
    }

    public static String getFileName(String path, String suffix) {
        path = FileUtil.normalize(path);
        return path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(suffix));
    }

    public static String getClassPackage(String path) {
        path = FileUtil.normalize(path);
        return path.substring(0, path.lastIndexOf(Constant.SUFFIX_CLASS)).replace(File.separator, ".");
    }
}
