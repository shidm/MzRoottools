package com.meizu.mzroottools.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.UserManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by linshen on 15/5/8.
 */
public class PhoneUtils {

    public static final String TAG = "PhoneUtils";

    private static final String CLASS_NAME_BUILD_EXT = "android.os.BuildExt";
    private static final String CLASS_NAME_CONTEXT_EXT = "android.content.ContextExt";

    /**
     * Define static constants here to avoid dummy repeat reflection
     */
    private static String sPhoneSn;
    private static String sPhoneModel;
    private static String sPhoneIMEI;
    private static Boolean sIsFlymeRom;
    private static Boolean sIsProductInternational;
    private static Boolean sIsPhoneRooted;
    private static Boolean sIsGuestMode;
    private static Boolean sIsIndiaLocale;

    private static Context context;

    public static void setContext(Context context) {
        PhoneUtils.context = context;
    }

    private PhoneUtils() {
        throw new AssertionError();
    }

    /**
     * 获得魅族手机的SN号
     *
     * @return
     */
    public synchronized static String getPhoneSn() {
        if (sPhoneSn == null) {
            sPhoneSn = SystemProperties.get(context, "ro.serialno");
        }
        Log.e(TAG, "Get Mz Phone SN " + sPhoneSn + "XXX");
        return sPhoneSn;
    }

    /**
     * 获得魅族手机的型号
     *
     * @return
     */
    public synchronized static String getPhoneModel() {
        if (TextUtils.isEmpty(sPhoneModel)) {
            if (isFlymeRom()) {
                sPhoneModel = Build.MODEL;
            } else {
                try {
                    Log.d(TAG, "getPhoneModel: not root");
                    sPhoneModel = (String) ReflectHelper.getStaticField(CLASS_NAME_BUILD_EXT, "MZ_MODEL");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(sPhoneModel) || sPhoneModel.toLowerCase().equals("unknown")) {
                Log.e(TAG, "get Mz Phone Model returns null or UNKNOWN");
                sPhoneModel = Build.MODEL;
            }
        }
        return sPhoneModel;
    }

    /**
     * 获得魅族手机的IMEI
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public synchronized static String getPhoneImei() {
        if (TextUtils.isEmpty(sPhoneIMEI)) {
            try {
                final String MZ_T_M = "android.telephony.MzTelephonyManager";
                final String METHOD_GET_DEVICE_ID = "getDeviceId";
                sPhoneIMEI = (String) ReflectHelper.invokeStatic(MZ_T_M, METHOD_GET_DEVICE_ID, null, null);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
            // 这个处理是非必须的，因为METHOD_GET_DEVICE_ID本身做了这个处理；为了运行在其它手机平台，这里兼容处理
            if (TextUtils.isEmpty(sPhoneIMEI)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                sPhoneIMEI = tm.getDeviceId();
            }
        }
        Log.e(TAG, "Get Mz Phone IMEI " + sPhoneIMEI);
        return sPhoneIMEI;
    }

    /**
     * 判断当前是否为Flyme
     *
     * @return
     */
    public synchronized static boolean isFlymeRom() {
        if (sIsFlymeRom != null) {
            return sIsFlymeRom;
        }
        try {
            boolean b = (Boolean) ReflectHelper.invokeStatic(CLASS_NAME_BUILD_EXT, "isFlymeRom", new Object[]{});
            sIsFlymeRom = b ? Boolean.TRUE : Boolean.FALSE;
            return sIsFlymeRom;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前是否为国际版
     *
     * @return
     */
    public synchronized static boolean isProductInternational() {
        if (sIsProductInternational != null) {
            return sIsProductInternational;
        }
        try {
            boolean b = (Boolean) ReflectHelper.invokeStatic(CLASS_NAME_BUILD_EXT, "isProductInternational", new Object[]{});
            sIsProductInternational = b ? Boolean.TRUE : Boolean.FALSE;
            return sIsProductInternational;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 判断手机是否为Root状态
     *
     * @param context
     * @return
     */
    public synchronized static boolean isPhoneRooted(Context context) {
        if (sIsPhoneRooted != null) {
            return sIsPhoneRooted;
        }
        String deviceStatus = "";
        try {
            deviceStatus = (String) ReflectHelper.getStaticField(CLASS_NAME_CONTEXT_EXT, "DEVICE_STATE_SERVICE");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //deviceStatus 为空则代表当前固件没有反射接口，此时可直接将 false 赋给缓存
        if (TextUtils.isEmpty(deviceStatus)) {
            Log.d(TAG, "deviceStatus: null");
            sIsPhoneRooted = Boolean.FALSE;
        } else {
            Object deviceStateManager = context.getSystemService(deviceStatus);
            if (deviceStateManager == null) {
                Log.d(TAG, "deviceStateManager: null");
                //deviceStateManager 在手机没有 boot complete 时会返回 null，此时暂时返回 false, 不将结果赋给缓存
                //但因为手机最终肯定会启动完成并且拿到 deviceStateManager，所以 sIsPhoneRooted 会在其不为空时再赋值
                return false;
            } else {
                try {
                    Integer result = (Integer) ReflectHelper.invoke(deviceStateManager, "doCheckState",
                            new Class[]{Integer.TYPE}, new Object[]{1});
                    sIsPhoneRooted = result == 1 ? Boolean.TRUE : Boolean.FALSE;
                } catch (Exception e) {
                    e.printStackTrace();
                    //反射失败暂时返回false，不更新缓存，之后调用可重新尝试反射
                    return false;
                }
            }
        }
        return sIsPhoneRooted;
    }

    /**
     * 判断访客模式是否开启
     *
     * @param context
     * @return
     */
    public synchronized static boolean isGuestModeEnabled(Context context) {
        if (sIsGuestMode != null) {
            return sIsGuestMode;
        }
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        try {
            //This method only works with Android 5.0
            boolean b = (Boolean) ReflectHelper.invoke(userManager, "isGuestUser", new Object[]{});
            sIsGuestMode = b ? Boolean.TRUE : Boolean.FALSE;
            return sIsGuestMode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isIndiaLocale(Context context) {
        if (sIsIndiaLocale == null) {
            sIsIndiaLocale = "india".equals(SystemProperties.get(String.valueOf(context), "ro.meizu.locale.region"));
        }
        return sIsIndiaLocale;
    }

    public synchronized static String getPsnAndChipId(Context context) {
        String psnAndChipId = null;
        String deviceStatus = "";
        try {
            deviceStatus = (String) ReflectHelper.getStaticField(CLASS_NAME_CONTEXT_EXT, "DEVICE_STATE_SERVICE");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(deviceStatus)) {
            Log.d(TAG, "deviceStatus: null");
            return psnAndChipId;
        } else {
            @SuppressLint("WrongConstant") Object deviceStateManager = context.getSystemService(deviceStatus);
            if (deviceStateManager == null) {
                Log.d(TAG, "deviceStateManager: NULL");
                return psnAndChipId;
            } else {
                try {
                    psnAndChipId = (String) ReflectHelper.invoke(deviceStateManager, "getPsnAndChipId", new Object[]{});
                } catch (Exception e) {
                    Log.e("cwj-root", "getPsnAndChipId E: " + e, e);
                    return psnAndChipId;
                }
                Log.d("cwj-root", "getPsnAndChipId -> " + psnAndChipId);
            }
        }
        return psnAndChipId;
    }

    public synchronized static String getRootSignatureCode(Context context) {
        String rootSignatureCode = null;
        String deviceStatus = "";
        try {
            deviceStatus = (String) ReflectHelper.getStaticField(CLASS_NAME_CONTEXT_EXT, "DEVICE_STATE_SERVICE");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(deviceStatus)) {
            return rootSignatureCode;
        } else {
            Log.d(TAG, "deviceStatus: " + deviceStatus);
            @SuppressLint("WrongConstant") Object deviceStateManager = context.getSystemService(deviceStatus);
            if (deviceStateManager == null) {
                Log.d(TAG, "deviceStateManager: NULL");
                return rootSignatureCode;
            } else {
                try {
                    rootSignatureCode = (String) ReflectHelper.invoke(deviceStateManager, "getRootSignatureCode", new Object[]{});
                } catch (Exception e) {
                    Log.e("cwj-root", "getRootSignatureCode E: " + e, e);
                    return rootSignatureCode;
                }
                Log.d("cwj-root", "getRootSignatureCode -> " + rootSignatureCode);
            }
        }
        return rootSignatureCode;
    }

    public synchronized static int setRootSignatureCode(Context context, byte[] rootSignatureCode) {
        int ret = -1;
        String deviceStatus = "";
        try {
            deviceStatus = (String) ReflectHelper.getStaticField(CLASS_NAME_CONTEXT_EXT, "DEVICE_STATE_SERVICE");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(deviceStatus)) {
            return ret;
        } else {
            Log.d(TAG, "deviceStatus: " + deviceStatus);
            @SuppressLint("WrongConstant") Object deviceStateManager = context.getSystemService(deviceStatus);
            if (deviceStateManager == null) {
                Log.d(TAG, "deviceStateManager: NULL");
                return ret;
            } else {
                try {
                    ret = (int) ReflectHelper.invoke(deviceStateManager, "setRootSignatureCode", rootSignatureCode);
                } catch (Exception e) {
                    Log.e("cwj-root", "setRootSignatureCode E: " + e, e);
                    return ret;
                }
                Log.d("cwj-root", "setRootSignatureCode -> " + rootSignatureCode + " | ret = " + ret);
            }
        }
        return ret;
    }
}
