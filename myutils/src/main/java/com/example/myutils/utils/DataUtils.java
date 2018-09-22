package com.example.myutils.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zheng on 2018/9/22 0022.
 */

public class DataUtils {

    public static String format(double in, int keepNum, boolean isRound) {
        DecimalFormat format = new DecimalFormat();
        if (keepNum < 0) {
            keepNum = 0;
        }

        format.setMaximumFractionDigits(keepNum);
        format.setMinimumFractionDigits(keepNum);
        format.setGroupingUsed(false);

        if (isRound) {
            // 四舍五入，负数原理同上
            format.setRoundingMode(RoundingMode.HALF_UP);
        }

        String result = format.format(in);
        return result;
    }

    public static String format(String in, int keepNum, boolean isRound) {
        String var3 = "";

        double indouble;
        try {
            indouble = Double.parseDouble(in);
        } catch (NumberFormatException var7) {
            var7.printStackTrace();
            return "";
        }

        return format(indouble, keepNum, isRound);
    }

}
