package com.sunny.test.views;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunny.test.R;
import com.sunny.test.wheelview.adapter.ArrayWheelAdapter;
import com.sunny.test.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 日期选择器
 * support: https://github.com/venshine/WheelView
 * 在原作者基础上做了少许修改，使得该控件支持三联动
 * Created by Sunny on 2017/8/18 0018.
 */

public class YouyunDatePickerDialog extends DialogFragment {

    @BindView(R.id.year_wheel_view)
    WheelView<String> yearWheelView;
    @BindView(R.id.month_wheel_view)
    WheelView<String> monthWheelView;
    @BindView(R.id.day_wheel_view)
    WheelView<String> dayWheelView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tv_delete)
    TextView tvCancel;
    @BindView(R.id.tv_cancel)
    TextView tvSure;
    Unbinder unbinder;

    private Context context = null;
    private View view = null;
    private int year, month, day;
    private OnYouyunDatePickerDialogClickListener listener = null;
    private List<String> yearList = null;
    private HashMap<String, List<String>> monthMap = null;
    private HashMap<String, List<String>> dayMap = null;
    private static final int ALLOW_YEAR_SPAN = 20;

    public YouyunDatePickerDialog() {
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private void setListener(OnYouyunDatePickerDialogClickListener listener) {
        this.listener = listener;
    }

    private void initCurrentTime(long currentTime) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static YouyunDatePickerDialog newInstance(Context context, long currentTime, OnYouyunDatePickerDialogClickListener listener) {
        Bundle args = new Bundle();
        YouyunDatePickerDialog fragment = new YouyunDatePickerDialog();
        fragment.setContext(context);
        fragment.setListener(listener);
        fragment.initCurrentTime(currentTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (view == null) {
            view = inflater.inflate(R.layout.youyun_date_picker_dialog, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = getResources().getColor(R.color.text_gray);
        style.textSize = 14;
        style.selectedTextSize = 14;
        style.holoBorderColor = Color.GRAY;

        yearList = createYear();
        yearWheelView.setWheelAdapter(new ArrayWheelAdapter<String>(context));
        yearWheelView.setSkin(WheelView.Skin.Holo);
        yearWheelView.setWheelData(yearList);
        yearWheelView.setStyle(style);

        monthMap = createMonth();
        monthWheelView.setWheelAdapter(new ArrayWheelAdapter<String>(context));
        monthWheelView.setSkin(WheelView.Skin.Holo);
        monthWheelView.setWheelData(monthMap.get(yearList.get(yearWheelView.getSelection())));
        monthWheelView.setStyle(style);
        yearWheelView.join(monthWheelView);
        yearWheelView.joinDatas(monthMap);

        dayMap = createDay();
        dayWheelView.setWheelAdapter(new ArrayWheelAdapter<String>(context));
        dayWheelView.setSkin(WheelView.Skin.Holo);
        dayWheelView.setWheelData(dayMap.get(yearList.get(yearWheelView.getSelection()) + "#" +
                monthMap.get(yearList.get(yearWheelView.getSelection()))
                        .get(monthWheelView.getSelection() % 12)));
        dayWheelView.setStyle(style);
        monthWheelView.join(dayWheelView);
        monthWheelView.joinDatas(dayMap);
        monthWheelView.joinParent(yearWheelView);
        monthWheelView.joinParentDatas(yearList);
    }

    public void resetDate() {
        if (yearWheelView != null)
            yearWheelView.setSelection(0);
        if (monthWheelView != null)
            monthWheelView.setSelection(0);
        if (dayWheelView != null)
            dayWheelView.setSelection(0);
    }

    private List<String> createYear() {
        List<String> yearList = new ArrayList<>();
        for (int i = year; i <= year + ALLOW_YEAR_SPAN; i++) {
            yearList.add(String.valueOf(i));
        }
        return yearList;
    }

    private HashMap<String, List<String>> createMonth() {
        HashMap<String, List<String>> monthMap = new HashMap<>();
        List<String> list_temp = null;
        list_temp = new ArrayList<>();
        for (int i = month; i <= 12; i++) {
            list_temp.add(String.valueOf(i));
        }
        monthMap.put(String.valueOf(year), list_temp);
        for (int i = year + 1; i <= year + ALLOW_YEAR_SPAN; i++) {
            list_temp = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                list_temp.add(String.valueOf(j + 1));
            }
            monthMap.put(String.valueOf(i), list_temp);
        }
        return monthMap;
    }

    private HashMap<String, List<String>> createDay() {
        HashMap<String, List<String>> dayMap = new HashMap<>();
        List<String> list_temp = null;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        list_temp = new ArrayList<>();
        for (int j = day; j <= calendar.getActualMaximum(Calendar.DATE); j++) {
            list_temp.add(String.valueOf(j));
        }
        dayMap.put(year + "#" + month, list_temp);

        for (int i = month + 1; i <= 12; i++) {
            calendar.set(Calendar.MONTH, i - 1);
            list_temp = new ArrayList<>();
            for (int j = 1; j <= calendar.getActualMaximum(Calendar.DATE); j++) {
                list_temp.add(String.valueOf(j));
            }
            dayMap.put(year + "#" + i, list_temp);
        }

        int dayCount = 0;
        for (int i = year + 1; i <= year + ALLOW_YEAR_SPAN; i++) {
            for (int j = 0; j < 12; j++) {
                list_temp = new ArrayList<>();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, j);
                dayCount = calendar.getActualMaximum(Calendar.DATE);
                for (int k = 0; k < dayCount; k++) {
                    list_temp.add(String.valueOf(k + 1));
                }
                dayMap.put(i + "#" + (j + 1), list_temp);
            }
        }
        return dayMap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_delete, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                if (listener != null)
                    listener.onCancel();
                break;
            case R.id.tv_cancel:
                int currentYear = Integer.valueOf(yearList.get(yearWheelView.getCurrentPosition()));
                int currentMonth = Integer.parseInt(monthMap.get(String.valueOf(currentYear)).get(monthWheelView.getCurrentPosition()));
                int currentDay = Integer.parseInt(dayMap.get(currentYear + "#" + currentMonth).get(dayWheelView.getCurrentPosition()));
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, currentYear);
                calendar.set(Calendar.MONTH, currentMonth - 1);
                calendar.set(Calendar.DAY_OF_MONTH, currentMonth);
                if (listener != null)
                    listener.onSure(currentYear, currentMonth, currentDay, calendar.getTimeInMillis());
                break;
        }
    }

    public interface OnYouyunDatePickerDialogClickListener {
        void onCancel();

        void onSure(int year, int month, int day, long timestamps);
    }
}
