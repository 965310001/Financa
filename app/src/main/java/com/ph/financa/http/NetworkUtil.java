package com.ph.financa.http;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.util.HashMap;
import java.util.Map;

import tech.com.commoncore.constant.ApiConstant;

/**
 * 网络请求二次的封装
 */
public class NetworkUtil {

    public static void doGet() {
    }

    public static void doPost(String url, Map<String, String> params, ACallback callback) {
        ViseHttp.POST(url)
                .addParams(params)
                .request(callback);
    }

    /*发送验证码*/
    public static void sendCode(String phone, ACallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        doPost(ApiConstant.SEND_CODE, params, callback);

        ViseHttp.POST(ApiConstant.SEND_CODE)
                .addParams(params).request(callback);
    }


}
