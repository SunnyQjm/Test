package com.sunny.test.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Sunny on 2017/9/8 0008.
 */

public class CustomLinerLayoutManager extends LinearLayoutManager{

    private boolean isScrollAble = true;

    public CustomLinerLayoutManager(Context context) {
        super(context);
    }

    public void setScrollAble(boolean scrollAble) {
        isScrollAble = scrollAble;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollAble && super.canScrollVertically();
    }
}
