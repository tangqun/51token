package com.sharex.token.api.util;

public class MoneyUtil {

    //限制字符串的长度
    public static String setLength(Double d, int num) {
        String str = d.toString();
        if (str == null || str.length() == 0) {
            return "--";
        }
        if(str.length() >= 8) {
            if (str.contains(".")) {
                String[] split = str.split("\\.");

                if(num == 0 || split[0].length() > 4){
                    return split[0];
                }

                StringBuffer buffer = new StringBuffer(split[1]);
                String s = buffer.toString();
                if (buffer.toString().length() > num) {
                    s = buffer.subSequence(0, num).toString();
                }

                return split[0] + "." + s;
            }
        }
        return str;
    }
}
