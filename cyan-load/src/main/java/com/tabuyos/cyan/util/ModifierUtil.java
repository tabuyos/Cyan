package com.tabuyos.cyan.util;

import org.objectweb.asm.Opcodes;

import java.util.StringJoiner;

/**
 * @Author Tabuyos
 * @Time 2020/4/4 15:26
 * @Site www.tabuyos.com
 * @Email tabuyos@outlook.com
 * @Description
 */
public class ModifierUtil {

    public static String getModifier(int mod) {
        StringJoiner sj = new StringJoiner(" ");
        if ((mod & Opcodes.ACC_PUBLIC) != 0) {
            sj.add("public");
        }

        if ((mod & Opcodes.ACC_PROTECTED) != 0) {
            sj.add("protected");
        }

        if ((mod & Opcodes.ACC_PRIVATE) != 0) {
            sj.add("private");
        }

        if ((mod & Opcodes.ACC_ABSTRACT) != 0) {
            sj.add("abstract");
        }

        if ((mod & Opcodes.ACC_STATIC) != 0) {
            sj.add("static");
        }

        if ((mod & Opcodes.ACC_FINAL) != 0) {
            sj.add("final");
        }

        if ((mod & Opcodes.ACC_TRANSIENT) != 0) {
            sj.add("transient");
        }

        if ((mod & Opcodes.ACC_VOLATILE) != 0) {
            sj.add("volatile");
        }

        if ((mod & Opcodes.ACC_SYNCHRONIZED) != 0) {
            sj.add("synchronized");
        }

        if ((mod & Opcodes.ACC_NATIVE) != 0) {
            sj.add("native");
        }

        if ((mod & Opcodes.ACC_STRICT) != 0) {
            sj.add("strictfp");
        }

        if ((mod & Opcodes.ACC_INTERFACE) != 0) {
            sj.add("interface");
        }

        return sj.toString();
    }
}
