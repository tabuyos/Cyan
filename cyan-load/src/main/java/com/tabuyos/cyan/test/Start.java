package com.tabuyos.cyan.test;

import com.tabuyos.cyan.util.ScanUtil;

/**
 * @Author Tabuyos
 * @Time 2020/4/3 16:20
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class Start {
    public static void main(String[] args) {
        ScanUtil scanUtil = new ScanUtil("F:/Temp/asm/fastjson-1.2.68.jar");
        scanUtil.parseJar();
    }
}
