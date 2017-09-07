package com.sunny.test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunny.test.R;

import butterknife.ButterKnife;

public class CustomViewStudyActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_study);
        ButterKnife.bind(this);

    }

}
