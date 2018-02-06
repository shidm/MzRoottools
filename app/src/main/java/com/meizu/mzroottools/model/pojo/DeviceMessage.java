package com.meizu.mzroottools.model.pojo;

public class DeviceMessage {

    private String deviceModel;
    private String deviceSn;
    private String deviceChipId;
    private String deviceImei;
    private boolean isPhoneRooted;
    private boolean isFlymeRom;

    public DeviceMessage() {
    }

    /**
     * @param deviceModel  设备型号
     * @param deviceSn     设备序列号
     * @param deviceChipId 设备CHIPID
     * @param deviceImei   设备IMEI码
     */
    public DeviceMessage(String deviceModel, String deviceSn, String deviceChipId, String deviceImei) {
        this.deviceModel = deviceModel;
        this.deviceSn = deviceSn;
        this.deviceChipId = deviceChipId;
        this.deviceImei = deviceImei;
    }

    /**
     * @param deviceModel   设备型号
     * @param deviceSn      设备序列号
     * @param deviceChipId  设备CHIPID
     * @param deviceImei    设备IMEI
     * @param isPhoneRooted 设备是否Root
     * @param isFlymeRom    设备是否是Flyme
     */
    public DeviceMessage(String deviceModel, String deviceSn, String deviceChipId
            , String deviceImei, boolean isPhoneRooted, boolean isFlymeRom) {
        this.deviceModel = deviceModel;
        this.deviceSn = deviceSn;
        this.deviceChipId = deviceChipId;
        this.deviceImei = deviceImei;
        this.isPhoneRooted = isPhoneRooted;
        this.isFlymeRom = isFlymeRom;
    }

    public boolean isPhoneRooted() {
        return isPhoneRooted;
    }

    public void setPhoneRooted(boolean phoneRooted) {
        isPhoneRooted = phoneRooted;
    }

    public boolean isFlymeRom() {
        return isFlymeRom;
    }

    public void setFlymeRom(boolean flymeRom) {
        isFlymeRom = flymeRom;
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

    public String getdeviceChipId() {
        return deviceChipId;
    }

    public void setdeviceChipId(String deviceChipId) {
        this.deviceChipId = deviceChipId;
    }

    public boolean haveAllMsg() {
        if (deviceModel == null || deviceSn == null
                || deviceChipId == null || deviceImei == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DeviceMessage{" +
                "deviceModel='" + deviceModel + '\'' +
                ", deviceSn='" + deviceSn + '\'' +
                ", deviceChipId='" + deviceChipId + '\'' +
                ", deviceImei='" + deviceImei + '\'' +
                ", isPhoneRooted=" + isPhoneRooted +
                ", isFlymeRom=" + isFlymeRom +
                '}';
    }
}
