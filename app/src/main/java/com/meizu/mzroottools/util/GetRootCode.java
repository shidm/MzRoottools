package com.meizu.mzroottools.util;

import android.util.Log;

import com.meizu.mzroottools.pojo.DeviceMessage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shidongming on 18-1-31.
 */

public class GetRootCode {

    //网络请求获取root码

    public static String getRootCodeFromNetwork(String url, DeviceMessage deviceMessage) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("imei", deviceMessage.getDeviceImei())
                .add("sn", deviceMessage.getDeviceSn())
                .add("chipid", deviceMessage.getdevicePsnAndChipId())
                .add("sign", getMd5(deviceMessage.getDeviceImei()
                        + deviceMessage.getDeviceSn()
                        + deviceMessage.getdevicePsnAndChipId())
                        + "签名密码")
                .build();
        Request request = new Request.Builder().method("GET",body).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求root码失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("onResponse: ", response.body().string());
            }
        });
        return null;
    }

    public static byte[] getMd5(String message) {
        MessageDigest md5 = null;
        byte[] newPwd = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            newPwd = md5.digest(message.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return newPwd;
    }
}
