package com.sunny.test.views.easy_refresh;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sunny on 2017/9/9 0009.
 */

public abstract class EasyRefreshFooter {
    private View footer;

    public EasyRefreshFooter(View footer) {
        this.footer = footer;
    }

    public void layout(int l, int t, int r, int b) {
        footer.layout(l, t, r, b);
    }

    public final int getMeasuredWidth() {
        return footer.getMeasuredWidth();
    }

    public final int getMeasuredHeight() {
        return footer.getMeasuredHeight();
    }

    public abstract void scrolling(int scrollDistance, int totalFooterHeight, float changeRate);

    public abstract void init();

    public abstract void loading();

    public abstract void loadFinish();

    public void setText(int id, String text) {
        View view = footer.findViewById(id);
        if(view instanceof TextView){
            ((TextView)view).setText(text);
        }
        if(view instanceof Button){
            ((Button)view).setText(text);
        }
    }

    public void setRotation(int id, float rotation) {
        footer.findViewById(id).setRotation(rotation);
    }

    public void setVisibility(int id, int visible) {
        footer.findViewById(id).setVisibility(visible);
    }

    public void setVisibility(int visible) {
        footer.setVisibility(visible);
    }
}
