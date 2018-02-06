package com.meizu.mzroottools.presenter.impl;

import android.content.Context;

import com.meizu.mzroottools.model.IDeviceMsg;
import com.meizu.mzroottools.model.impl.DeviceMsgImpl;
import com.meizu.mzroottools.presenter.IShowDeviceMessagePresenter;
import com.meizu.mzroottools.ui.IGetDevMsg;

public class ShowDeviceMessagePresenter implements IShowDeviceMessagePresenter {

    private IGetDevMsg iGetDevMsg;
    private IDeviceMsg iDeviceMsg;

    private static final String TAG = "ShowDeviceMessagePresenter-->";

    public ShowDeviceMessagePresenter(IGetDevMsg iGetDevMsg) {
        this.iGetDevMsg = iGetDevMsg;
    }

    @Override
    public void showDevMsg(Context context) {
        iDeviceMsg = new DeviceMsgImpl();
        iGetDevMsg.show(iDeviceMsg.getDeviceMessage(context, false));
    }
}
