package com.sunny.test.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sunny on 2017/9/6 0006.
 */

public class MyGameView extends View implements Runnable, View.OnClickListener{

    private Paint mPaint = null;
    public MyGameView(Context context) {
        this(context, null);
    }

    public MyGameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //创建一个抗锯齿的Paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setOnClickListener(this);
        new Thread(this).start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public void run() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    @Override
    public void onClick(View v) {

    }
}
