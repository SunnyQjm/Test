package com.sunny.test.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunny.test.R;
import com.sunny.test.utils.DensityUtil;

/**
 * Created by Sunny on 2017/8/17 0017.
 */

public class EasyBar extends RelativeLayout {
    private String title;
    @Dimension
    private int title_size;
    @ColorInt
    private int title_color;
    @DrawableRes
    private int leftIcon;
    @DrawableRes
    private int rightIcon;
    @Dimension
    private int icon_size;
    @Dimension
    private int icon_margin;


    private ImageView img_left;
    private ImageView img_right;
    private TextView tv_title;
    private OnEasyBarClickListener mListener = null;

    public EasyBar(Context context) {
        this(context, null);
    }

    public EasyBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        //left icon
        img_left = new ImageView(context);
        img_left.setImageResource(leftIcon);
        img_left.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LayoutParams img_left_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        img_left_param.addRule(CENTER_VERTICAL);
        img_left_param.addRule(ALIGN_PARENT_LEFT);
        img_left_param.width = icon_size;
        img_left_param.height = icon_size;
        img_left_param.leftMargin = icon_margin;
        img_left.setLayoutParams(img_left_param);
        addView(img_left, img_left_param);

        img_left.setOnClickListener(view -> {
            if (mListener != null)
                mListener.onLeftIconClick(img_left);
        });

        //center title
        tv_title = new TextView(context);
        tv_title.setText(title);
        tv_title.setTextSize(title_size);
        tv_title.setTextColor(title_color);
        LayoutParams tv_title_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_title_param.addRule(CENTER_IN_PARENT);
        tv_title.setLayoutParams(tv_title_param);
        addView(tv_title, tv_title_param);


        //right icon
        img_right = new ImageView(context);
        img_right.setImageResource(rightIcon);
        img_right.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LayoutParams img_right_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        img_right_param.addRule(CENTER_VERTICAL);
        img_right_param.addRule(ALIGN_PARENT_RIGHT);
        img_right_param.width = icon_size;
        img_right_param.height = icon_size;
        img_right_param.rightMargin = icon_margin;
        img_right.setLayoutParams(img_right_param);
        addView(img_right, img_right_param);

        img_right.setVisibility(INVISIBLE);
        img_right.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onRightIconClick(img_right);
            }
        });
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EasyBar);
        title = ta.getString(R.styleable.EasyBar_title);
        title_size = DensityUtil.px2sp(context,
                ta.getDimensionPixelSize(R.styleable.EasyBar_title_size,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics())));
        title_color = ta.getColor(R.styleable.EasyBar_title_color, getResources().getColor(R.color.text_gray));
        leftIcon = ta.getResourceId(R.styleable.EasyBar_left_icon, R.drawable.back);
        rightIcon = ta.getResourceId(R.styleable.EasyBar_right_icon, R.drawable.icon_add);
        icon_size = ta.getDimensionPixelSize(R.styleable.EasyBar_icon_size, DensityUtil.dip2px(context, 24));
        icon_margin = ta.getDimensionPixelOffset(R.styleable.EasyBar_icon_margin, DensityUtil.dip2px(context, 12));
        ta.recycle();
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setOnEasyBarClickListener(OnEasyBarClickListener listener) {
        this.mListener = listener;
    }

    public void setLeftIconVisible() {
        if (img_left != null)
            img_left.setVisibility(VISIBLE);
    }

    public void setLeftIconInVisible() {
        if (img_left != null)
            img_left.setVisibility(INVISIBLE);
    }

    public void setRightIconVisible() {
        if (img_right != null)
            img_right.setVisibility(VISIBLE);
    }

    public void setRightIconInVisible() {
        if (img_right != null)
            img_right.setVisibility(INVISIBLE);
    }

    public void setRightIcon(Drawable drawable) {
        if (img_right != null)
            img_right.setImageDrawable(drawable);
    }

    public void setRightIcon(Bitmap bitmap){
        if(img_right != null)
            img_right.setImageBitmap(bitmap);
    }

    public void setRightIcon(@DrawableRes int res){
        if(img_right != null)
            img_right.setImageResource(res);
    }

    public interface OnEasyBarClickListener {
        void onLeftIconClick(View view);

        void onRightIconClick(View view);
    }
}
