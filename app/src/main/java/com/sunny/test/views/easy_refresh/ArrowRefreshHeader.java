package com.sunny.test.views.easy_refresh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.test.R;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Sunny on 2017/9/10 0010.
 */

public class ArrowRefreshHeader extends EasyRefreshHeaderHandler {

    public ArrowRefreshHeader(int headResourceId) {
        super(headResourceId);
    }

    @Override
    public void getView(LayoutInflater inflater, ViewGroup viewGroup) {
        System.out.println("getView");
        inflater.inflate(headerResourceId, viewGroup, true);
    }

    @Override
    public void scrolling(View header, int scrollDistance, int totalHeaderHeight) {
        if (scrollDistance > (totalHeaderHeight * changeRate)) {
            System.out.println("scrolling1");
            this.setText(header, R.id.refresh_text, "释放刷新");
            this.setRotation(header, R.id.refresh_arrow, 180f);
            this.setVisibility(header, R.id.refresh_text, VISIBLE);
            this.setVisibility(header, R.id.refresh_progressBar, INVISIBLE);
            this.setVisibility(header, R.id.refresh_arrow, VISIBLE);
        } else {
            System.out.println("scrolling2");
            this.setText(header, R.id.refresh_text, "下拉刷新");
            this.setRotation(header, R.id.refresh_arrow, 0f);
            this.setVisibility(header, R.id.refresh_text, VISIBLE);
            this.setVisibility(header, R.id.refresh_progressBar, INVISIBLE);
            this.setVisibility(header, R.id.refresh_arrow, VISIBLE);
        }
    }

    @Override
    public void init(View header) {
        System.out.println("init");
        this.setText(header, R.id.refresh_text, "下拉刷新");
        this.setRotation(header, R.id.refresh_arrow, 0f);
        this.setVisibility(header, R.id.refresh_text, VISIBLE);
        this.setVisibility(header, R.id.refresh_progressBar, INVISIBLE);
        this.setVisibility(header, R.id.refresh_arrow, VISIBLE);
    }

    @Override
    public void refreshing(View header) {
        System.out.println("refreshing");
        this.setText(header, R.id.refresh_text, "正在刷新");
        this.setVisibility(header, R.id.refresh_text, INVISIBLE);
        this.setVisibility(header, R.id.refresh_progressBar, VISIBLE);
        this.setVisibility(header, R.id.refresh_arrow, INVISIBLE);
    }

    @Override
    public void refreshFinish(View header) {
        System.out.println("refresh finish");
        init(header);
    }
}
