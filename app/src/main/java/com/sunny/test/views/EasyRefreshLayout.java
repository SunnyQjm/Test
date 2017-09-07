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
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    /**
     * 移动比率，比率越高，下拉相对距离越小，如果等于1，则手指下拉多少距离，就实际下拉多少距离
     */
    private float MOVE_RATE = 1.5f;

    /**
     * 箭头转向比例，如果等于1则表示要头布局完全下拉箭头才转向
     */
    private float changeRate = 0.8f;

    @LayoutRes
    private int headerResourceId = R.layout.easy_refresh_header;

    private View header;
    private View contentView;
    private ImageView refreshArrow;
    private TextView refreshText;
    private ProgressBar refreshProgressBar;
    private LayoutInflater inflater;
    private OnRefreshListener listener;

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
        NORMAL, SCROLL_DOWN, SCROLL_DOWN_EFFECT, REFRESHING
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
        refreshArrow = (ImageView) header.findViewById(R.id.refresh_arrow);
        refreshText = (TextView) header.findViewById(R.id.refresh_text);
        refreshProgressBar = (ProgressBar) header.findViewById(R.id.refresh_progressBar);
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
        if (changed && contentView != null) {
            System.out.println("onLayout");
            //将头部署在当前视图之外
            header.layout(0, -header.getMeasuredHeight(), getWidth(), header.getHeight());
            //部署内容
            contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
            headerHeight = header.getMeasuredHeight();
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 如果返回true，则会拦截下触摸事件，不向下传递，由本ViewGroup消费
     * 如果返回false或者执行super，则不拦截，向下传递
     *
     * @param ev
     * @return
     */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = ev.getRawY();
                mYLastMove = mYDown;
                updateViewContent();
                break;
            case MotionEvent.ACTION_MOVE:
                mYMove = ev.getRawY();
                float diff = Math.abs(mYMove - mYDown);
                mYLastMove = mYMove;
                //当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                //这样就不会响应子控件的点击或者长按事件
                if (isMoveAble() && diff > mTouchSlop) {
                    status = Status.SCROLL_DOWN;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                System.out.println("ACTION_MOVE：" + status);
                mYMove = event.getRawY();
                //自按下滚动的距离
                int scrollDistance = Math.abs(getScrollY());
                int deltaY = (int) (mYLastMove - mYMove);

                //根据下滑的比例，改变显示的视图
                if (scrollDistance > (headerHeight * changeRate)) {
                    status = Status.SCROLL_DOWN_EFFECT;
                    updateViewContent();
                } else {
                    status = Status.SCROLL_DOWN;
                    updateViewContent();
                }

                //如果当前布局向下拉出头布局，并且头布局向下滚动的距离小于等于头布局的高度
                //或者当前头布局已经拉出，但是正在回滚
                //两种情况下都执行滚动操作
                if ((getScrollY() <= 0 && getScrollY() >= -headerHeight)
                        || (getScrollY() <= 0 && deltaY > 0)) {
                    scrollBy(0, (int) (deltaY / MOVE_RATE));
                }
                mYLastMove = mYMove;
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTION_UP");
                System.out.println("headerHeight: " + headerHeight);
                if (status == Status.SCROLL_DOWN_EFFECT) {
                    showRefresh();
                } else {
                    closeRefresh();
                }
                updateViewContent();
                //如果当前处于正常状态，则相应点击事件
                if (status == Status.NORMAL)
                    performClick();
                break;
        }
        return true;
    }

    private void showRefresh() {
        int dy = -headerHeight - getScrollY();
        status = Status.REFRESHING;
        mScroller.startScroll(0, getScrollY(), 0, dy);
        invalidate();
        if(listener != null)
            listener.onRefresh();
    }

    private void updateViewContent() {
        switch (status) {
            case NORMAL:
            case SCROLL_DOWN:
                refreshText.setText("下拉刷新");
                refreshArrow.setRotation(0f);
                refreshText.setVisibility(VISIBLE);
                refreshProgressBar.setVisibility(INVISIBLE);
                refreshArrow.setVisibility(VISIBLE);
                break;
            case REFRESHING:
                refreshText.setText("正在刷新");
                refreshText.setVisibility(INVISIBLE);
                refreshProgressBar.setVisibility(VISIBLE);
                refreshArrow.setVisibility(INVISIBLE);
                break;
            case SCROLL_DOWN_EFFECT:
                refreshText.setText("释放刷新");
                refreshArrow.setRotation(180f);
                refreshText.setVisibility(VISIBLE);
                refreshProgressBar.setVisibility(INVISIBLE);
                refreshArrow.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void computeScroll() {
        //如果还在滚动过程中
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


    private boolean isMoveAble() {
        if (contentView == null)
            return false;
        //如果当前在顶部，而且是下滑动作
        if (isChildScrollToTop() && (mYMove - mYDown) > 0) {
            return true;
        }
        return false;
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




    public interface OnRefreshListener{
        void onRefresh();
    }

    //////////////////////////////////////////////////////////////////
    ///////  以下是对外的接口
    //////////////////////////////////////////////////////////////////

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public void closeRefresh(){
        int dy = -getScrollY();
        status = Status.NORMAL;
        mScroller.startScroll(0, getScrollY(), 0, dy);
        invalidate();
    }
}
