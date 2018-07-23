package com.sharex.token.api.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StringUtil {

    public static String ReplaceByMosaic(String mobileNum) {

         return mobileNum.substring(0, 3) + "****" + mobileNum.substring(7, 11);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String toQueryString(Map<String, String> params) throws UnsupportedEncodingException {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                sb.append(key + "=" + value);// URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20"));
            } else {
                sb.append(key + "=" + value + "&");// URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20") + "&");
            }
        }
        return sb.toString();
    }
}
