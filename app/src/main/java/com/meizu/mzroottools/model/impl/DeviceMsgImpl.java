package com.meizu.mzroottools.model.impl;

import android.content.Context;
import android.util.Log;

import com.meizu.mzroottools.model.IDeviceMsg;
import com.meizu.mzroottools.model.pojo.DeviceMessage;
import com.meizu.mzroottools.util.PhoneUtils;

public class DeviceMsgImpl implements IDeviceMsg {

    private static DeviceMessage deviceMessage = null;

    @Override
    public DeviceMessage getDeviceMessage(Context context, boolean isNeedRefresh) {
        if (deviceMessage != null && isNeedRefresh) {
            return deviceMessage;
        } else {
            deviceMessage = new DeviceMessage(PhoneUtils.getPhoneModel()
                    , PhoneUtils.getPhoneSn(), PhoneUtils.getPsnAndChipId(context).substring(16)
                    , PhoneUtils.getPhoneImei(), PhoneUtils.isPhoneRooted(context)
                    , PhoneUtils.isFlymeRom());
            Log.d("getDeviceMessage: ", PhoneUtils.getPsnAndChipId(context).substring(16));
        }
        return deviceMessage;
    }

    @Override
    public String getRootCode(Context context) {
        return PhoneUtils.getRootSignatureCode(context);
    }

    @Override
    public int setRootCode(Context context, byte[] bytes) {
        return PhoneUtils.setRootSignatureCode(context, bytes);
    }

    @Override
    public boolean isRooted(Context context) {
        return PhoneUtils.isPhoneRooted(context);
    }
}
