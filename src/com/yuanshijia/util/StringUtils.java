package com.yuanshijia.util;

/**
 * @author yuan
 * @date 2019/10/22
 * @description
 */
public class StringUtils {
    public static boolean isEmpty(String s){
        if (s == null || s.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
}
