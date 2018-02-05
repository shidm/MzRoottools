package com.meizu.mzroottools.pojo;

/**
 * Created by shidongming on 18-1-31.
 */

public class DeviceMessage {
    private String deviceModel;
    private String deviceSn;
    private String devicePsnAndChipId;
    private String deviceImei;

    public DeviceMessage() {
    }

    /**
     * @param deviceModel 设备型号
     * @param deviceSn 设备序列号
     * @param devicePsnAndChipId 设备CHIPID码
     * @param deviceImei 设备IMEI码
     */
    public DeviceMessage(String deviceModel, String deviceSn, String devicePsnAndChipId, String deviceImei) {
        this.deviceModel = deviceModel;
        this.deviceSn = deviceSn;
        this.devicePsnAndChipId = devicePsnAndChipId;
        this.deviceImei = deviceImei;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getdeviceModel() {
        return deviceModel;
    }

    public void setdeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getdevicePsnAndChipId() {
        return devicePsnAndChipId;
    }

    public void setdevicePsnAndChipId(String devicePsnAndChipId) {
        this.devicePsnAndChipId = devicePsnAndChipId;
    }

    public boolean haveAllMsg() {
        if (deviceModel == null || deviceSn == null
                || devicePsnAndChipId == null || deviceImei == null) {
            return false;
        }
        return true;
    }
}
