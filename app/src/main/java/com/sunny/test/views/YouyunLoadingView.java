package com.sunny.test.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.sunny.test.R;

/**
 * Created by Sunny on 2017/8/28 0028.
 */

public class YouyunLoadingView extends ProgressDialog{
    public YouyunLoadingView(Context context) {
        this(context, R.style.YouyunLoadingView);
    }

    public YouyunLoadingView(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.loading_view_layout);
        Window window = getWindow();
        if(window == null)
            return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
}
