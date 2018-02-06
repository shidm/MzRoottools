package com.meizu.mzroottools.ui.fragment;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.mzroottools.R;
import com.meizu.mzroottools.presenter.IRootPresenter;
import com.meizu.mzroottools.presenter.impl.RootPresenter;
import com.meizu.mzroottools.ui.IUnlockDev;

public class UnLockDevFragment extends Fragment implements IUnlockDev, View.OnClickListener {

    private View view;
    private ImageView unlock_img;
    private TextView unlock_msg_mark, unlock_msg_prompt;
    private LinearLayout unlock_msg;
    private Button button;
    private ProgressBar progressBar;
    private RelativeLayout unWaitUI;

    private static final String TAG = "UnLockDevFragment-->";

    private static final String URL = "http://mroot.flyme.cn//api/v1/service/getcode";

    private IRootPresenter presenter;

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
        progressBar = view.findViewById(R.id.requestProgressBar);
        unWaitUI = view.findViewById(R.id.unWaitUI);
        button = view.findViewById(R.id.unlock_button);
        button.setOnClickListener(this);

        presenter = new RootPresenter(this, getContext());

        //是否已经Root
        presenter.isRooted(getContext());
    }

    /**
     * @param isRoot 解锁是否成功
     */
    private void hintView(boolean isRoot) {
        if (isRoot) {
            //已经获取了权限
            Log.d(TAG, String.valueOf(isRoot));
            unlock_img.setImageResource(R.mipmap.ic_launcher_round);
            unlock_msg_mark.setText("解锁成功");
            unlock_msg_prompt.setText("已成功获取本机的Root权限");

            unlock_msg.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
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
                unWaitUI.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                //网络请求获取root码并设置root码解锁设备
                presenter.getAndSaveRootCode(URL, getContext());
                break;
            default:
                break;
        }
    }

    @Override
    public void getRootCode(boolean isSuccess) {
        Log.d(TAG, "saveRootCode: " + isSuccess);
        progressBar.setVisibility(View.GONE);
        unWaitUI.setVisibility(View.VISIBLE);
        hintView(isSuccess);
        if (!isSuccess) {
            Toast.makeText(getContext(), "解锁失败请确认已审核通过", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isRooted(boolean isRoot) {
        hintView(isRoot);
    }
}
