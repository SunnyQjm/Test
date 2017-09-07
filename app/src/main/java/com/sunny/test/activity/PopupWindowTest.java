package com.sunny.test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sunny.test.R;
import com.sunny.test.views.YouyunLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupWindowTest extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.btn_test)
    Button button2;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poup_window_test);
        ButterKnife.bind(this);

        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_window_bottom_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_cancel);
        textView.setOnClickListener(v -> {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        });
        TextView textView1 = (TextView) view.findViewById(R.id.tv_delete);
        textView1.setOnClickListener(v -> {
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupWindowStyle);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setSplitTouchEnabled(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
    }

    @OnClick({R.id.button, R.id.btn_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                new YouyunLoadingView(this).show();
//                if (popupWindow.isShowing()) {
//                    popupWindow.dismiss();
//                } else {
//                    popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                }
                break;
            case R.id.btn_test:
                Toast.makeText(this, "test btn click", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
