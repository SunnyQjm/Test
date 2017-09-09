package com.sunny.test.views.easy_refresh;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sunny on 2017/9/9 0009.
 */

public abstract class EasyRefreshHeader {
    private View header;

    public EasyRefreshHeader(View header) {
        this.header = header;
    }


    public void layout(int l, int t, int r, int b) {
        header.layout(l, t, r, b);
    }

    public final int getMeasuredWidth() {
        return header.getMeasuredWidth();
    }

    public final int getMeasuredHeight() {
        return header.getMeasuredHeight();
    }

    public abstract void scrolling(int scrollDistance, int totalHeaderHeight, float changeRate);

    public abstract void init();

    public abstract void refreshing();

    public abstract void refreshFinish();

    public void setText(int id, String text) {
        View view = header.findViewById(id);
        if(view instanceof TextView){
            ((TextView)view).setText(text);
        }
        if(view instanceof Button){
            ((Button)view).setText(text);
        }
    }

    public void setRotation(int id, float rotation) {
        header.findViewById(id).setRotation(rotation);
    }

    public void setVisibility(int id, int visible) {
        header.findViewById(id).setVisibility(visible);
    }
}
