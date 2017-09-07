package com.sunny.test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sunny.test.R;
import com.sunny.test.adapter.EasyStringAdapter;
import com.sunny.test.views.EasyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CustomViewStudyActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;

    private EasyStringAdapter adapter;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_study);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        for (int i = 0; i < 30; i++) {
            datas.add("item" + i);
        }
        adapter = new EasyStringAdapter(datas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);

        refreshLayout.setOnRefreshListener(() -> {
            Observable.create(e -> {
                for (int i = 0; i < 3; i++) {
                    datas.add(0, "add item" + i);
                    Thread.sleep(800);
                }
                e.onNext(new byte[0]);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        adapter.notifyDataSetChanged();
                        refreshLayout.closeRefresh();
                    });
        });
    }

}
