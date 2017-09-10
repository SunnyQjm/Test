package com.sunny.test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.sunny.test.R;
import com.sunny.test.adapter.EasyStringAdapter;
import com.sunny.test.views.easy_refresh.ArrowRefreshHeader;
import com.sunny.test.views.easy_refresh.EasyRefreshFooter;
import com.sunny.test.views.easy_refresh.EasyRefreshFooterHandler;
import com.sunny.test.views.easy_refresh.EasyRefreshHeaderHandler;
import com.sunny.test.views.easy_refresh.EasyRefreshLayout;

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
        for (int i = 0; i < 5; i++) {
            datas.add("item" + i);
        }
        adapter = new EasyStringAdapter(datas);
        CustomLinerLayoutManager linerLayoutManager = new CustomLinerLayoutManager(this);
        recyclerView.setLayoutManager(linerLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);

        EasyRefreshHeaderHandler header = new ArrowRefreshHeader(R.layout.easy_refresh_header);
        refreshLayout.setHeader(header);

        EasyRefreshFooterHandler footer = new EasyRefreshFooter(R.layout.easy_refresh_footer);
        refreshLayout.setFooter(footer);
        refreshLayout.setOnRefreshListener(() -> {
            linerLayoutManager.setScrollAble(false);
            Observable.create(e -> {
                        for (int i = 0; i < 4; i++) {
                            datas.add(0, "add item" + i);
                            Thread.sleep(500);
                        }
                        e.onNext(new byte[0]);
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                adapter.notifyDataSetChanged();
                                refreshLayout.closeRefresh();
                                linerLayoutManager.setScrollAble(true);
                            });
                }
        );

        refreshLayout.setOnLoadListener(() -> Observable.create(e -> {
            linerLayoutManager.setScrollAble(false);
            for (int i = 0; i < 4; i++) {
                datas.add("load item" + i);
                Thread.sleep(500);
            }
            e.onNext(new byte[0]);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    adapter.notifyDataSetChanged();
                    refreshLayout.closeLoad();
                    linerLayoutManager.setScrollAble(true);
                    loadFinish();
                }));

    }

    private void loadFinish() {
        View view = createFooterView();
        adapter.setFooterView(view);
        refreshLayout.setLoadAble(false);
    }

    private View createFooterView() {
        return LayoutInflater.from(this).inflate(R.layout.easy_refresh_end, null, false);
    }

}
