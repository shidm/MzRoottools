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


public class RootPresenter implements IRootPresenter {

    private IUnlockDev iUnlockDev;
    private IDeviceMsg iDeviceMsg;
    private IGetRootCode iGetRootCode;
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("handleMessage: ", "接收消息成功");
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
    public String getRootCode(Context context) {
        return iDeviceMsg.getRootCode(context);
    }

    private void isFailed(GetRootCodeBackMessage getRootCodeBackMessage) {
        if (getRootCodeBackMessage.getCode().equals("200")) {
            iDeviceMsg.setRootCode(context, getRootCodeBackMessage.getValue().getCode().getBytes());
            iUnlockDev.getRootCode(true);
        } else {
            Log.d("handleMessage: ", getRootCodeBackMessage.toString());
            iUnlockDev.getRootCode(false);
        }
    }
}
