package com.sunny.test.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sunny.test.R;

/**
 * Created by Sunny on 2017/9/6 0006.
 */

public class EasyRefreshLayout extends LinearLayout implements View.OnTouchListener {


    private View header = null;
    private RecyclerView recyclerView;

    private int headerHeight;
    private LayoutParams headerLayoutParams;
    /**
     * 是否允许刷新
     */
    private boolean isAbleToRefresh = true;
    public EasyRefreshLayout(Context context) {
        this(context, null);
    }

    public EasyRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        header = LayoutInflater.from(context).inflate(R.layout.easy_refresh_header, this, false);
        setOrientation(VERTICAL);
        addView(header);
    }

    /**
     * 在布局中隐藏头
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            System.out.println("---------------------onLayout-------------------");
            headerHeight = header.getHeight();
            headerLayoutParams = (LayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin = -headerHeight;
            recyclerView = (RecyclerView) getChildAt(1);
            recyclerView.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setIsAbleToRefresh(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    public void setIsAbleToRefresh(MotionEvent event) {
        View firstChild = recyclerView.getChildAt(0);
        //没有元素的时候是允许下拉刷新的
        if(firstChild == null){
            isAbleToRefresh = true;
        } else {
//            int firstVisiblePos =
        }
    }
}
