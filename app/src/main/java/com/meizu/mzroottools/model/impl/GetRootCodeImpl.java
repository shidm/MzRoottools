package com.meizu.mzroottools.model.impl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.meizu.mzroottools.model.IGetRootCode;
import com.meizu.mzroottools.model.pojo.DeviceMessage;
import com.meizu.mzroottools.model.pojo.GetRootCodeBackMessage;
import com.meizu.mzroottools.util.PhoneUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetRootCodeImpl implements IGetRootCode {

    private static final String TAG = "GetRootCodeImpl-->";

    //网络请求获取Root码

    @Override
    public void getRootCodeFromNetwork(String url, DeviceMessage deviceMessage
            , final Handler handler) {
        PhoneUtils.getRootCodeFromNetwork(url,deviceMessage,handler);
    }
}
