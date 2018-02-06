package com.meizu.mzroottools.model;

import android.os.Handler;
import com.meizu.mzroottools.model.pojo.DeviceMessage;

public interface IGetRootCode {
    void getRootCodeFromNetwork(String url, DeviceMessage deviceMessage
            , Handler handler);
}
