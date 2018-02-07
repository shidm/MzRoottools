package com.meizu.mzroottools.model;

import android.content.Context;

import com.meizu.mzroottools.model.pojo.DeviceMessage;


public interface IDeviceMsg {
    DeviceMessage getDeviceMessage(Context context, boolean isNeedRefresh);
    byte[] getRootCode(Context context);
    int setRootCode(Context context, byte[] bytes);
    boolean isRooted(Context context);
}
