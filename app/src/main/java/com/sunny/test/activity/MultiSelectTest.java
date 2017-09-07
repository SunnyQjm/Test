package com.sunny.test.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunny.test.R;
import com.sunny.test.adapter.EasyStringAdapter;
import com.sunny.test.base.BaseQuickAdapter;
import com.sunny.test.utils.DensityUtil;
import com.sunny.test.views.EasyBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultiSelectTest extends AppCompatActivity {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    private List<String> data;
    private EasyStringAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_select_test);
        ButterKnife.bind(this);
        buildData();
        initView();
    }

    private void buildData() {
        data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("item " + i);
        }
    }

    private void initView() {
        easyBar.setTitle("多选测试");
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                tvDelete.setVisibility(View.VISIBLE);
                tvSelectAll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        adapter = new EasyStringAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showSelect();
                return true;
            }
        });
    }

    private void showSelect() {
        ObjectAnimator.ofFloat(tvDelete, "translationY", DensityUtil.dip2px(this, 30), 0)
                .setDuration(300)
                .start();
        tvDelete.setVisibility(View.VISIBLE);
        tvSelectAll.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(tvSelectAll, "alpha", 0f, 1f)
                .setDuration(300)
                .start();
    }

    @OnClick({R.id.tv_select_all, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_all:
                break;
            case R.id.tv_delete:
                hideSelect();
                break;
        }
    }

    private void hideSelect() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvDelete, "translationY", 0,
                DensityUtil.dip2px(this, 30))
                .setDuration(300);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvDelete.setVisibility(View.GONE);
            }
        });
        objectAnimator.start();
        tvSelectAll.setVisibility(View.GONE);
        tvSelectAll.setAlpha(0);
    }
}
