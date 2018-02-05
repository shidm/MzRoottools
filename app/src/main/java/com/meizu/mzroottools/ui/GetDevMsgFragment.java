package com.meizu.mzroottools.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.account.oauth.MzAccountUtil;
import com.meizu.account.oauth.MzAuthenticator;
import com.meizu.account.oauth.OnMzAuthListener;
import com.meizu.mzroottools.BuildConfig;
import com.meizu.mzroottools.R;
import com.meizu.mzroottools.pojo.DeviceMessage;
import com.meizu.mzroottools.util.PhoneUtils;

import java.lang.reflect.Method;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class GetDevMsgFragment extends Fragment implements View.OnClickListener {

    private TextView go_login;
    private TextView no_permission;
    private CardView getMsgCard;
    private TextView dev_mark, dev_msg_msg, dev_msg_prompt;
    private ImageView dev_msg_img;
    private RelativeLayout dev_msg_father;
    public static DeviceMessage deviceMessagesg = null;

    private View view;

    private String msg = "请先在设备上登录Flyme账号";
    private static String[] PERMISSIONS = {
            Manifest.permission.GET_ACCOUNTS
    };
    private static final int PERMISSIONS_STORAGE = 123;//权限请求码

    private static final String TAG = "获取设备信息";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.get_device_msg, null);

        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        checkPermissions();
    }

    private void init() {

        no_permission = view.findViewById(R.id.no_permission);

        getMsgCard = view.findViewById(R.id.getMsgCard);
//        ViewGroup.LayoutParams params = getMsgCard.getLayoutParams();
//        params.height = params.width ;
//        getMsgCard.setLayoutParams(params);
        getMsgCard.setOnClickListener(this);

        go_login = view.findViewById(R.id.go_login);
        //这个一定要记得设置，不然点击不生效
        go_login.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder spannable = new SpannableStringBuilder(msg);
        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {
                                  Toast.makeText(getContext(), "点击了登录", Toast.LENGTH_SHORT).show();
                                  MzAuthenticator authenticator = new MzAuthenticator(getActivity(), "basic");
                                  authenticator.getAuthToken(true, false, getActivity(), new OnMzAuthListener() {
                                      @Override
                                      public void onHandleIntent(Intent intent) {

                                      }

                                      @Override
                                      public void onError(int i, String s, String s1) {

                                      }

                                      @Override
                                      public void onSuccess(String s, String s1) {

                                      }
                                  });
                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(Color.BLUE);
                              }
                          }, 6, 8
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        go_login.setText(spannable);

        dev_mark = view.findViewById(R.id.dev_msg_mark);
        dev_msg_father = view.findViewById(R.id.dev_msg_father);
        dev_msg_img = view.findViewById(R.id.dev_msg_img);
        dev_msg_msg = view.findViewById(R.id.dev_msg_message);
        dev_msg_prompt = view.findViewById(R.id.dev_msg_prompt);
        dev_msg_prompt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getMsgCard:
                //获取信息
                getDevMsg();
                //Toast.makeText(getContext(), "获取设备信息：" + Build.MODEL + getSerialNumber(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.dev_msg_prompt:
                Toast.makeText(getContext(), "重新获取数据", Toast.LENGTH_SHORT).show();
                getDevMsg();
                break;
            default:
                break;
        }
    }

    private void getDevMsg() {
//        Log.d(TAG, "getRootCode: "
//                + PhoneUtils.getRootSignatureCode(getContext())
//                + "______"
//                + PhoneUtils.isFlymeRom()
//                + "______"
//                + PhoneUtils.getPsnAndChipId(getContext()));
        hintView(2);
        deviceMessagesg = new DeviceMessage(PhoneUtils.getPhoneModel(), PhoneUtils.getPhoneSn()
                , PhoneUtils.getPsnAndChipId(getContext()), PhoneUtils.getPhoneImei());
        //判断设备信息是否获取完整
        if (!deviceMessagesg.haveAllMsg()) {
            dev_msg_img.setImageResource(R.mipmap.ic_launcher_round);
            dev_mark.setText("获取失败");
            dev_mark.setTextColor(Color.BLACK);
            dev_msg_msg.setText("\n\n\n\n\n\n\n\n设备信息异常,请联系客服人员");
            dev_msg_msg.setTextColor(Color.BLACK);
            dev_msg_msg.setGravity(Gravity.CENTER_HORIZONTAL);
            dev_msg_prompt.setText("重新获取");
            dev_msg_prompt.setGravity(Gravity.CENTER_HORIZONTAL);
            dev_msg_prompt.setTextColor(Color.BLUE);
            dev_msg_prompt.setClickable(true);
        } else {
            dev_msg_img.setImageResource(R.mipmap.ic_launcher_round);
            dev_mark.setText("获取成功");
            dev_mark.setTextColor(Color.BLACK);
            dev_msg_msg.setText("您的设备信息如下\n\n" + "设备型号："
                    + deviceMessagesg.getdeviceModel() + "\n\n"
                    + "SN号：" + deviceMessagesg.getDeviceSn() + "\n\n"
                    + "CHPID：" + deviceMessagesg.getdevicePsnAndChipId());
            dev_msg_msg.setTextColor(Color.BLACK);
            dev_msg_prompt.setText("请至网页申请页填写以上设备信息提交解锁申请");
            dev_msg_prompt.setTextColor(Color.GRAY);
            dev_msg_prompt.setClickable(false);
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = checkSelfPermission(getContext(), PERMISSIONS[0]);//检查是否有权限（此处读写权限获取其一就可以）
            if (permission != PackageManager.PERMISSION_GRANTED) {

                this.requestPermissions(
                        PERMISSIONS,
                        PERMISSIONS_STORAGE
                );
            } else
                checkLogin();
        } else
            checkLogin();

    }

    private void checkLogin() {
        if (MzAccountUtil.hasMzAccount(getContext())) {
            Log.d(TAG, "已经登录");
            hintView(0);
        } else {
            Log.d(TAG, "未登录");
            hintView(1);
        }
    }

    private void hintView(int code) {
        switch (code) {
            case 0:
                go_login.setVisibility(View.GONE);
                dev_msg_father.setVisibility(View.GONE);
                no_permission.setVisibility(View.GONE);
                getMsgCard.setVisibility(View.VISIBLE);
                break;
            case 1:
                getMsgCard.setVisibility(View.GONE);
                dev_msg_father.setVisibility(View.GONE);
                no_permission.setVisibility(View.GONE);
                go_login.setVisibility(View.VISIBLE);
                break;
            case 2:
                getMsgCard.setVisibility(View.GONE);
                go_login.setVisibility(View.GONE);
                no_permission.setVisibility(View.GONE);
                dev_msg_father.setVisibility(View.VISIBLE);
                break;
            case 3:
                getMsgCard.setVisibility(View.GONE);
                go_login.setVisibility(View.GONE);
                dev_msg_father.setVisibility(View.GONE);
                no_permission.setVisibility(View.VISIBLE);
                no_permission.setText("请前往：\n" + "手机管家->权限管理->应用权限管理->Root工具\n"
                        + "开启读取联系人权限");
                no_permission.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            default:
                break;
        }
    }

    private void gotoMeizuPermission() {

        //打开应用权限管理界面

        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //打开应用信息界面

/*        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package",getContext().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getContext().getPackageName());
        }
        startActivity(localIntent);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[0])) {
                        //没有点击不再提醒
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                                .setMessage("未取得您的联系人权限,“Root工具”无法正常使用,请前往应用权限设置打开权限")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                })
                                .setPositiveButton("去打开", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        gotoMeizuPermission();
                                    }
                                }).setCancelable(false);
                        alertDialog.create().show();
                    }
                    hintView(3);
                }
                break;
            default:
                break;
        }
    }
}
