package com.sunny.test.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.MotionEventCompat;
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
    private float moveRate = 1.5f;

    /**
     * 箭头转向比例，如果等于1则表示要头布局完全下拉箭头才转向
     */
    private float changeRate = 0.8f;

    @LayoutRes
    private int headerResourceId = R.layout.easy_refresh_header;

    @LayoutRes
    private int footerResourceId = R.layout.easy_refresh_footer;

    @LayoutRes
    private int endResourceId = R.layout.easy_refresh_end;

    private View header;
    private View contentView;
    private View footer;
    private TextView footerText;
    private ImageView headerArrow;
    private TextView headerText;
    private ProgressBar headerProgressBar;
    private LayoutInflater inflater;
    private OnRefreshListener refreshListener;
    private OnLoadListener loadListener;

    /**
     * 是否允许下拉或上滑
     */
    private boolean enable = true;

    /**
     * 是否允许下拉刷新
     * 如果enable = false，则该参数失效
     */
    private boolean isRefreshAble = true;

    /**
     * 内部条件，同上
     */
    private boolean innerIsRefreshAble = true;

    /**
     * 是否允许上拉加载更多
     * 如果enable = false，则该参数失效
     */
    private boolean isLoadAble = true;

    /**
     * 内部条件，同上
     */
    private boolean innerIsLoadAble = true;

    /**
     * 是否当数据填充慢屏幕才允许上拉加载更多，默认是
     */
    private boolean loadOnlyDataFullScreen = true;

    /**
     * 头布局的上边界，用于回弹
     */
    private int headerHeight;

    /**
     * 本布局所占高度
     */
    private int layoutHeight;

    /**
     * 内容布局的高度
     */
    private int contentViewHeight;

    /**
     * 尾布局的高度
     */
    private int footerHeight;

    /**
     * 当前状态
     */
    private Status status = Status.NORMAL;

    /**
     * 使用模式，默认是开启下拉刷新和上拉加载更多
     */
    private Mode mode = Mode.BOTH;

    public enum Mode {
        REFRESH_ONLY, LOAD_ONLY, BOTH, NONE
    }

    enum Status {
        NORMAL, SCROLL_DOWN, SCROLL_DOWN_EFFECT, REFRESHING,
        SCROLL_UP, LOADING
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
        headerArrow = (ImageView) header.findViewById(R.id.refresh_arrow);
        headerText = (TextView) header.findViewById(R.id.refresh_text);
        headerProgressBar = (ProgressBar) header.findViewById(R.id.refresh_progressBar);

        //添加加载布局
        inflater.inflate(footerResourceId, this, true);
        footer = getChildAt(getChildCount() - 1);
        footerText = (TextView) footer.findViewById(R.id.footer_text);

        //添加数据加载结束布局
        inflater.inflate(endResourceId, this, true);

        footer.setVisibility(INVISIBLE);
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
        if (contentView != null) {
            System.out.println("onLayout");
            //将头部署在当前视图之外
            header.layout(0, -header.getMeasuredHeight(), getWidth(), header.getMeasuredHeight());
            //部署内容
            contentView.layout(0, 0, getWidth(), getHeight());

            //部署尾布局
            footer.layout(0, contentView.getMeasuredHeight(), getWidth(),
                    contentView.getMeasuredHeight() + footer.getMeasuredHeight());

            headerHeight = header.getMeasuredHeight();
            footerHeight = footer.getMeasuredHeight();
            layoutHeight = getHeight();
            contentViewHeight = contentView.getMeasuredHeight();
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
        dealMulTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mYDown = ev.getRawY();
//                mYLastMove = mYDown;
//                updateViewContent();
                break;
            case MotionEvent.ACTION_MOVE:
//                mYMove = ev.getRawY();
                float diff = Math.abs(mYMove - mYDown);
                mYLastMove = mYMove;

                //当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                //这样就不会响应子控件的点击或者长按事件
                if (isMoveAble() && diff > mTouchSlop && mode != Mode.NONE) {
                    if (mYMove - mYDown > 0 && isRefreshAble &&
                            (mode == Mode.BOTH || mode == Mode.REFRESH_ONLY)) {
                        status = Status.SCROLL_DOWN;
                    } else if (isLoadAble && (mode == Mode.BOTH || mode == Mode.LOAD_ONLY)) {
                        status = Status.SCROLL_UP;
                        //上拉加载更多的时候显示底部布局
                        footer.setVisibility(VISIBLE);
                    }
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dealMulTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                System.out.println("ACTION_MOVE：" + status);
//                mYMove = event.getRawY();
                //自按下滚动的距离
                int scrollDistance = Math.abs(getScrollY());
                int deltaY = (int) (mYLastMove - mYMove);

                switch (status) {
                    case SCROLL_DOWN_EFFECT:
                    case SCROLL_DOWN:
                        //根据下滑的比例，改变显示的视图
                        if (scrollDistance > (headerHeight * changeRate)) {
                            status = Status.SCROLL_DOWN_EFFECT;
                            updateViewContent();
                        } else {
                            status = Status.SCROLL_DOWN;
                            updateViewContent();
                        }

                        System.out.println("getScrollY: " + getScrollY());
                        if (getScrollY() > 0) {
                            status = Status.NORMAL;
                            updateViewContent();
                            scrollTo(0, 0);
                            event.setAction(MotionEvent.ACTION_DOWN);
                            dispatchTouchEvent(event);
                        }
                        //如果当前布局向下拉出头布局，并且头布局向下滚动的距离小于等于头布局的高度
                        //或者当前头布局已经拉出，但是正在回滚
                        //两种情况下都执行滚动操作
                        if ((getScrollY() <= 0 && getScrollY() >= -headerHeight)
                                || (getScrollY() <= 0 && deltaY > 0)) {
                            scrollBy(0, (int) (deltaY / moveRate));
                        }
                        break;
                    case SCROLL_UP:
                        System.out.println("getScrollY: " + getScrollY());
                        if (getScrollY() < 0) {
                            status = Status.NORMAL;
                            updateViewContent();
                            scrollTo(0, 0);
                            event.setAction(MotionEvent.ACTION_DOWN);
                            dispatchTouchEvent(event);
                        }

                        if ((getScrollY() >= 0 && getScrollY() <= footerHeight)
                                || (getScrollY() >= 0 && deltaY < 0)) {
                            scrollBy(0, (int) (deltaY / moveRate));
                        }
                        break;
                }
                mYLastMove = mYMove;
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTION_UP");
                System.out.println("headerHeight: " + headerHeight);
                switch (status) {
                    case SCROLL_DOWN_EFFECT:
                        showRefresh();
                        break;
                    case REFRESHING:
                        break;
                    case SCROLL_UP:
                    case LOADING:
                        showLoading();
                        break;
                    case SCROLL_DOWN:
                    case NORMAL:
                        closeRefresh();
                        break;
                }
                updateViewContent();
                //如果当前处于正常状态，则相应点击事件
                if (status == Status.NORMAL)
                    performClick();
                break;
        }
        return true;
    }

    /**
     * 处理多点触控的情况，准确地计算Y坐标和移动距离dy
     * 同时兼容单点触控的情况
     */
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

    public void dealMulTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                mYDown = ev.getY(pointerIndex);
                mYLastMove = mYDown;
                updateViewContent();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                mYMove = ev.getY(pointerIndex);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = ev.getPointerId(pointerIndex);
                //有新的点按下，就将新点算作有效点
                if (pointerId != mActivePointerId) {
                    float distance = mYLastMove - mYDown;
                    mYLastMove = ev.getY(pointerIndex);
                    mYDown = mYLastMove - distance;
                    mActivePointerId = ev.getPointerId(pointerIndex);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                //如果有效点抬起，就重新选择有效点
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    float distance = mYLastMove - mYDown;
                    mYLastMove = ev.getY(newPointerIndex);
                    mYDown = mYLastMove - distance;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }
    }

    private void showLoading() {
        int dy = footerHeight - getScrollY();
        status = Status.LOADING;
        mScroller.startScroll(0, getScrollY(), 0, dy);
        invalidate();
        callLoad();
    }

    private void showRefresh() {
        int dy = -headerHeight - getScrollY();
        status = Status.REFRESHING;
        mScroller.startScroll(0, getScrollY(), 0, dy);
        invalidate();
        callRefresh();
    }

    private void callLoad() {
        innerIsLoadAble = false;
        if (loadListener != null)
            loadListener.onLoad();
    }

    private void callRefresh() {
        innerIsRefreshAble = false;
        if (refreshListener != null)
            refreshListener.onRefresh();
    }

    private void updateViewContent() {
        switch (status) {
            case NORMAL:
            case SCROLL_DOWN:
                headerText.setText("下拉刷新");
                headerArrow.setRotation(0f);
                headerText.setVisibility(VISIBLE);
                headerProgressBar.setVisibility(INVISIBLE);
                headerArrow.setVisibility(VISIBLE);
                break;
            case REFRESHING:
                headerText.setText("正在刷新");
                headerText.setVisibility(INVISIBLE);
                headerProgressBar.setVisibility(VISIBLE);
                headerArrow.setVisibility(INVISIBLE);
                break;
            case SCROLL_DOWN_EFFECT:
                headerText.setText("释放刷新");
                headerArrow.setRotation(180f);
                headerText.setVisibility(VISIBLE);
                headerProgressBar.setVisibility(INVISIBLE);
                headerArrow.setVisibility(VISIBLE);
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
        if (enable && isRefreshAble && innerIsRefreshAble && isChildScrollToTop() && (mYMove - mYDown) > 0) {
            return true;
        }

        if (enable && (!loadOnlyDataFullScreen || contentViewHeight >= layoutHeight)
                && isLoadAble && innerIsLoadAble && isChildScrollToBottom() && (mYMove - mYDown) < 0) {
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

    /**
     * 判断contentView是否已经滑到底部
     *
     * @return
     */
    private boolean isChildScrollToBottom() {
        return !ViewCompat.canScrollVertically(contentView, 1);
    }


    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadListener {
        void onLoad();
    }

    //////////////////////////////////////////////////////////////////
    ///////  以下是对外的接口
    //////////////////////////////////////////////////////////////////

    /**
     * 设置刷新监听
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.refreshListener = listener;
    }

    /**
     * 设置加载更多监听
     *
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    /**
     * 结束刷新
     */
    public void closeRefresh() {
        int dy = -getScrollY();
        status = Status.NORMAL;
        innerIsRefreshAble = true;
        mScroller.startScroll(0, getScrollY(), 0, dy);
        invalidate();
        footer.setVisibility(INVISIBLE);
    }

    /**
     * 结束加载
     */
    public void closeLoad() {
        int dy = -getScrollY();
        status = Status.NORMAL;
        innerIsLoadAble = true;
        mScroller.startScroll(0, getScrollY(), 0, dy);
        footer.setVisibility(INVISIBLE);
        invalidate();
    }

    /**
     * 设置使用模式
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * 设置改向的比例
     *
     * @param changeRate
     */
    public void setChangeRate(float changeRate) {
        this.changeRate = changeRate;
    }

    /**
     * 设置头尾滚动的距离与实际手滑动距离的比例
     * 如果为1，则表示手移动多少头就滑动多少
     *
     * @param moveRate
     */
    public void setMoveRate(float moveRate) {
        this.moveRate = moveRate;
    }

    /**
     * 刷新加载功能是否可用
     *
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 是否允许上拉加载更多
     *
     * @param loadAble
     */
    public void setLoadAble(boolean loadAble) {
        isLoadAble = loadAble;
    }

    /**
     * 是否允许下拉刷新
     *
     * @param refreshAble
     */
    public void setRefreshAble(boolean refreshAble) {
        isRefreshAble = refreshAble;
    }
}
