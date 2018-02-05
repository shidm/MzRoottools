package com.meizu.mzroottools.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.mzroottools.R;
import com.meizu.mzroottools.util.GetRootCode;
import com.meizu.mzroottools.util.PhoneUtils;

public class UnLockDevFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView unlock_img;
    private TextView unlock_msg_mark, unlock_msg_prompt;
    private LinearLayout unlock_msg;
    private Button button;

    private static final String TAG = "解锁设备";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.unlock_device, null);

        init();
        return view;
    }

    private void init() {
        unlock_img = view.findViewById(R.id.unlock_img);
        unlock_msg_mark = view.findViewById(R.id.unlock_msg_mark);
        unlock_msg_prompt = view.findViewById(R.id.unlock_msg_prompt);
        unlock_msg = view.findViewById(R.id.unlock_success);
        button = view.findViewById(R.id.unlock_button);
        button.setOnClickListener(this);

        hintView(PhoneUtils.isPhoneRooted(getContext()));
    }

    /**
     * @param isRoot 解锁是否成功
     */
    private void hintView(boolean isRoot){
        if (isRoot) {
            //已经获取了权限
            Log.d(TAG, String.valueOf(isRoot));
            unlock_img.setImageResource(R.mipmap.ic_launcher_round);
            unlock_msg_mark.setText("解锁成功");
            unlock_msg_prompt.setText("已成功获取本机的Root权限");

            unlock_msg.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }else {
            //没有获取
            Log.d(TAG, String.valueOf(isRoot));
            unlock_img.setImageResource(R.mipmap.ic_launcher_round);

            unlock_msg.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unlock_button:
                //网络请求获取root码并设置root码解锁设备
                Toast.makeText(getContext(),"解锁",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "isRoot: "+PhoneUtils.isPhoneRooted(getContext()));
                if (GetDevMsgFragment.deviceMessagesg != null) {
                    GetRootCode.getRootCodeFromNetwork("172.16.185.122",GetDevMsgFragment.deviceMessagesg);
                }
                break;
            default:
                break;
        }
    }
}
