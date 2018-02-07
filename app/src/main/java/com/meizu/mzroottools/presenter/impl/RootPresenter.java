package com.meizu.mzroottools.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.meizu.mzroottools.model.IDeviceMsg;
import com.meizu.mzroottools.model.IGetRootCode;
import com.meizu.mzroottools.model.impl.DeviceMsgImpl;
import com.meizu.mzroottools.model.impl.GetRootCodeImpl;
import com.meizu.mzroottools.model.pojo.GetRootCodeBackMessage;
import com.meizu.mzroottools.presenter.IRootPresenter;
import com.meizu.mzroottools.ui.IUnlockDev;

import java.io.UnsupportedEncodingException;


public class RootPresenter implements IRootPresenter {

    private IUnlockDev iUnlockDev;
    private IDeviceMsg iDeviceMsg;
    private IGetRootCode iGetRootCode;
    private Context context;

    private static final String TAG = "RootPresenter-->";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "接收消息成功");
            isFailed((GetRootCodeBackMessage) msg.obj);
        }
    };

    public RootPresenter(IUnlockDev iUnlockDev, Context context) {
        this.iUnlockDev = iUnlockDev;
        iDeviceMsg = new DeviceMsgImpl();
        iGetRootCode = new GetRootCodeImpl();
        this.context = context;
    }

    @Override
    public void getAndSaveRootCode(String url, Context context) {
        iGetRootCode.getRootCodeFromNetwork(url, iDeviceMsg.getDeviceMessage(context, false)
                , handler);
    }

    @Override
    public void isRooted(Context context) {
        iUnlockDev.isRooted(iDeviceMsg.isRooted(context));
    }

    @Override
    public byte[] getRootCode(Context context) {
        return iDeviceMsg.getRootCode(context);
    }

    /**
     * 请求root码是否成功
     *
     * @param getRootCodeBackMessage 网络请求返回信息
     */
    private void isFailed(GetRootCodeBackMessage getRootCodeBackMessage) {
        try {
            iDeviceMsg.setRootCode(context,"123abcd".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (getRootCodeBackMessage.getCode().equals("200")) {
            Log.d(TAG, "请求root码成功");
            iDeviceMsg.setRootCode(context, getRootCodeBackMessage.getValue().getCode().getBytes());
            iUnlockDev.getRootCode(true);
        } else {
            /*
            * 请求数据失败
            * code：
            * 198001->接口签名错误
            * 120015->imei错误
            * 120016->sn错误
            * 120017->chipid错误
            * 120019->解锁吗不存在
            * */
            Log.d(TAG, "请求root码失败,错误码:"+getRootCodeBackMessage.getCode());
            Log.d(TAG, "RootCode: "+iDeviceMsg.getRootCode(context));
            iUnlockDev.getRootCode(false);
        }
    }
}
