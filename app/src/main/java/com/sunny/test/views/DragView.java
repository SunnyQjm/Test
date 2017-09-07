package com.sunny.test.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by Sunny on 2017/9/6 0006.
 */

public class DragView extends android.support.v7.widget.AppCompatTextView implements View.OnTouchListener {

    private float lastX;
    private float lastY;
    private boolean isBeginDrag = false;

    //触摸时间最小移动的距离

    private final int touchSlop = 5;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("ACTION_DOWN");
                lastX = event.getX();
                lastY = event.getY();
                isBeginDrag = true;
                //如果当前View有父视图，则调用下面的方法可以使其父视图不会打断当前event
                if (getParent() != null)
                    getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                float curY = event.getY();
                int deltaX = (int) (lastX - curX);
                int deltaY = (int) (lastY - curY);
//                if (!isBeginDrag && (Math.abs(deltaX) > touchSlop || Math.abs(deltaY) > touchSlop)){
//                    isBeginDrag = true;
//                    if(deltaX > 0)
//                }
                System.out.println("ACTION_MOVE");
                System.out.println(deltaX);
                System.out.println(deltaY);
                if (Math.abs(deltaX) > touchSlop || Math.abs(deltaY) > touchSlop) {
                    scrollBy(deltaX, deltaY);
                    lastX = curX;
                    lastY = curY;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                System.out.println("ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTION_UP");
                break;
        }
        return true;
    }
}
