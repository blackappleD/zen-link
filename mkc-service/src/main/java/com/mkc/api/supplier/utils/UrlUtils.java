package com.mkc.api.supplier.utils;

import com.alibaba.fastjson2.JSONObject;

/**
 *
 *
 * @author linst
 * @date 2024/11/22
 */
public class UrlUtils {
    public static String getUrl(JSONObject params, String url) {
        StringBuilder urlParam = new StringBuilder();
        for (String key : params.keySet()) {
            urlParam.append(key).append("=").append(params.getString(key)).append("&");
        }
        String urlP = urlParam.substring(0, urlParam.toString().length() - 1);
        return url + "?" + urlP;
    }
}
