package com.sunny.test.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.sunny.test.R;

/**
 * Created by Sunny on 2017/9/6 0006.
 */

public class EasyRefreshLayout extends ViewGroup {


    /**
     * 用于完成滚动操作
     */
    private OverScroller mScroller;

    /**
     * 判定为滚动的最小距离
     */
    private int mTouchSlop;

    /**
     * 手机按下时的屏幕坐标
     */
    private float mYDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mYMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mYLastMove;


    @LayoutRes
    private int headerResourceId = R.layout.easy_refresh_header;

    private View header;
    private View contentView;
    private LayoutInflater inflater;

    /**
     * 是否允许下拉
     */
    private boolean enable = true;

    /**
     * 头布局的上边界，用于回弹
     */
    private int headerHeight;

    /**
     * 当前状态
     */
    private Status status = Status.NORMAL;

    enum Status {
        NORMAL, SCROLLING
    }

    public EasyRefreshLayout(Context context) {
        this(context, null);
    }

    public EasyRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);

        //获取inflater，用来加载头布局
        inflater = LayoutInflater.from(context);

        mScroller = new OverScroller(context);
        //获取touchSlop
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    /**
     * 初始化自定义参数
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        //TODO init param
    }

    /**
     * 从XML文件中加载一个布局后，当所有View均add完毕，会调用该方法
     */
    @Override
    protected void onFinishInflate() {
        System.out.println("onFinishInflate");
        super.onFinishInflate();
        contentView = getChildAt(0);
        contentView.setPadding(0, 0, 0, 0);

        //将头布局加载到当前布局
        inflater.inflate(headerResourceId, this, true);
        header = getChildAt(getChildCount() - 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("onMeasure");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //为每一个子控件计算size
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println("onLayout");
        //将头部署在当前视图之外
        header.layout(0, -header.getMeasuredHeight(), getWidth(), getHeight());
        //部署内容
        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        headerHeight = header.getMeasuredHeight();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = event.getRawY();
                mYLastMove = mYDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mYMove = event.getRawY();
                int deltaY = (int) (mYLastMove - mYMove);
                scrollBy(0, deltaY);
                mYLastMove = mYMove;
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("headerHeight: " + headerHeight);
                int dy = - getScrollY();
                mScroller.startScroll(0, getScrollY(), 0, dy);
                invalidate();
                //如果当前处于正常状态，则相应点击事件
                if (status == Status.NORMAL)
                    performClick();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        //如果还在滚动过程中
        if (mScroller.computeScrollOffset()) {
            System.out.println("scroll to：" + mScroller.getCurrX() + ", " + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 如果返回true，则会拦截下触摸事件，不向下传递，由本ViewGroup消费
     *
     * @param ev
     * @return
     */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mYDown = ev.getRawX();
                mYLastMove = mYDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mYMove = ev.getRawX();
                float diff = Math.abs(mYMove - mYDown);
                mYLastMove = mYMove;
                //当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                //这样就不会响应子控件的点击或者长按事件
                if(diff > mTouchSlop){
                    return true;
                }
                break;
        }
        return enable && isChildScrollToTop();
    }


    /**
     * 判断contentView是否已经滑到顶部
     *
     * @return
     */
    private boolean isChildScrollToTop() {
        //如果contentView无法再继续向上滑则表示已经滑到顶部
        return !ViewCompat.canScrollVertically(contentView, -1);
    }
}
