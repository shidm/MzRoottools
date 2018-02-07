package com.meizu.mzroottools.presenter;

import android.content.Context;

public interface IRootPresenter {
    void getAndSaveRootCode(String url, Context context);
    void isRooted(Context context);
    byte[] getRootCode(Context context);
}
