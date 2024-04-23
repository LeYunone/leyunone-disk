package com.leyunone.disk.util;

import com.leyunone.disk.model.ResponseCode;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2021-12-27 09:50
 */
public class AssertUtil {

    public static void isFalse(boolean condition, String message) {
        isFalse(condition, new RuntimeException(message));
    }

    public static void isFalse(boolean condition, ResponseCode message) {
        isFalse(condition, new RuntimeException(message.getMessage()));
    }

    public static void isFalse(boolean condition, RuntimeException ex) {
        isTrue(!condition, ex);
    }

    public static void isTrue(boolean condition, RuntimeException ex) {
        if (!condition) {
            throw ex;
        }
    }
}
