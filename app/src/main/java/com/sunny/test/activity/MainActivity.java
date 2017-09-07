package com.sunny.test.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.sunny.test.R;
import com.sunny.test.views.YouyunDatePickerDialog;
import com.sunny.test.wheelview.adapter.ArrayWheelAdapter;
import com.sunny.test.wheelview.widget.WheelView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Demo
 *
 * @author venshine
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    private WheelView hourWheelView, minuteWheelView, secondWheelView;
    private YouyunDatePickerDialog datePickerDialog = null;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Glide.with(this)
                .asBitmap()
                .load("data:image/jpeg;base64,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")
                .into(new BitmapImageViewTarget(imgAvatar){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        imgAvatar.setImageDrawable(roundedBitmapDrawable);
                    }
                });
        initWheel2();
    }

    @OnClick(R.id.img_avatar)
    public void onViewClicked() {

    }

    /**
     * holo皮肤
     */
    private void initWheel2() {
        hourWheelView = (WheelView) findViewById(R.id.hour_wheelview);
        hourWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        hourWheelView.setSkin(WheelView.Skin.Holo);
        hourWheelView.setWheelData(createHours());
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        style.holoBorderColor = Color.GRAY;
        hourWheelView.setStyle(style);
        hourWheelView.setLoop(true);
        hourWheelView.setExtraText("时", Color.parseColor("#0288ce"), 40, 70);

        minuteWheelView = (WheelView) findViewById(R.id.minute_wheelview);
        minuteWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        minuteWheelView.setSkin(WheelView.Skin.Holo);
        minuteWheelView.setWheelData(createMinutes());
        minuteWheelView.setStyle(style);
        minuteWheelView.setLoop(true);
        minuteWheelView.setExtraText("分", Color.parseColor("#0288ce"), 40, 70);

        secondWheelView = (WheelView) findViewById(R.id.second_wheelview);
        secondWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        secondWheelView.setSkin(WheelView.Skin.Holo);
        secondWheelView.setWheelData(createMinutes());
        secondWheelView.setStyle(style);
        secondWheelView.setLoop(true);
        secondWheelView.setExtraText("秒", Color.parseColor("#0288ce"), 40, 70);
    }

    public void showDialog(View view) {
        if (datePickerDialog == null)
            datePickerDialog = YouyunDatePickerDialog.newInstance(this, System.currentTimeMillis(),
                    new YouyunDatePickerDialog.OnYouyunDatePickerDialogClickListener() {
                        @Override
                        public void onCancel() {
                            if (datePickerDialog != null)
                                datePickerDialog.dismiss();
                        }

                        @Override
                        public void onSure(int year, int month, int day, long timestamps) {
                            if (datePickerDialog != null)
                                datePickerDialog.dismiss();
                            Toast.makeText(MainActivity.this,
                                    "year: " + year + " month: " + month + " day: " + day,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        if (datePickerDialog.isHidden())
            return;
        datePickerDialog.show(getFragmentManager(), "tag");
    }


    private ArrayList<String> createHours() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createArrays() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("item" + i);
        }
        return list;
    }


}