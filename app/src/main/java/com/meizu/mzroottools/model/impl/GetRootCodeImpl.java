package com.meizu.mzroottools.model.impl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.meizu.mzroottools.model.IGetRootCode;
import com.meizu.mzroottools.model.pojo.DeviceMessage;
import com.meizu.mzroottools.model.pojo.GetRootCodeBackMessage;

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

public class GetRootCodeImpl implements IGetRootCode {

    String RULE_KEY = "saf.CTR#214mz";

    @Override
    public void getRootCodeFromNetwork(String url, DeviceMessage deviceMessage
            , final Handler handler) {
        String sign = getMd5(deviceMessage.getDeviceImei()
                + deviceMessage.getDeviceSn()
                + deviceMessage.getdeviceChipId()
                + RULE_KEY);

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("imei", deviceMessage.getDeviceImei())
                .add("sn", deviceMessage.getDeviceSn())
                .add("chipid", deviceMessage.getdeviceChipId())
                .add("sign", sign)
                .build();
        Log.d("getRootCodeFromNetwork:", sign);

        Request request = new Request.Builder().url(url).method("POST", body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求root码失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String data = response.body().string();
                Log.d("onResponse: ", data);
                Gson gson = new Gson();
                GetRootCodeBackMessage msg = gson.fromJson(data
                        , GetRootCodeBackMessage.class);
                Message message = Message.obtain();
                message.obj = msg;
                handler.sendMessage(message);
            }
        });
    }

    public static String getMd5(String message) {
        MessageDigest md5;
        byte[] newPwd = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            newPwd = md5.digest(message.getBytes());
            Log.d("getMd5: ", message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < newPwd.length; i++) {
            if (Integer.toHexString(0xFF & newPwd[i]).length() == 1)
                s.append("0").append(Integer.toHexString(0xFF & newPwd[i]));
            else
                s.append(Integer.toHexString(0xFF & newPwd[i]));
        }
        return s.toString();
    }
}
