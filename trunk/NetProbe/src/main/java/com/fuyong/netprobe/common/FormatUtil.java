/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:2014/7/16 15:32
 */
package com.fuyong.netprobe.common;

import java.text.NumberFormat;

public class FormatUtil {
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    static {
        numberFormat.setMaximumFractionDigits(2);
    }

    public static String formatNumber(Object obj) {
        return formatNumber(obj, "");
    }

    public static String formatNumber(Object obj, String defValue) {
        if (null == obj) {
            Object o = obj;
            return defValue;
        }
        return numberFormat.format(obj);
    }
}
