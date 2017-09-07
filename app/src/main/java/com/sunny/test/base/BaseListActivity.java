package com.sunny.test.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.sunny.test.R;
import com.sunny.test.views.EasyBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseListActivity extends AppCompatActivity {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.text)
    TextView text;

    private BaseListAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle("List Test");
        easyBar.setLeftIconInVisible();


        text.setText(Html.fromHtml("<a href = \"http://www.baidu.com\">百度</a>"));
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add("item" + i);
//        }
//        adapter = new BaseListAdapter<>(list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        adapter.bindToRecyclerView(recyclerView);
//        adapter.setOnLoadMoreListener(() ->
//                Observable.create((ObservableOnSubscribe<Integer>) e -> {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException err) {
//                        err.printStackTrace();
//                    }
//                    int size = list.size();
//                    for (int i = 0; i < 20; i++) {
//                        list.add("item" + (size + i));
//                    }
//                    e.onNext(size);
//                })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(integer -> adapter.loadMoreComplete()), recyclerView);
//        refresh.setColorSchemeResources(
//                R.color.blue,
//                R.color.text_red,
//                R.color.colorAccent,
//                R.color.triangle
//        );
//        refresh.setOnRefreshListener(() -> {
//            recyclerView.postDelayed(() -> {
//                List<String> add = new ArrayList<>();
//                for (int i = 0; i < 20; i++) {
//                    add.add("addItem" + i);
//                }
//                adapter.addData(0, add);
//                refresh.setRefreshing(false);
//                adapter.notifyDataSetChanged();
//                recyclerView.scrollToPosition(0);
//            }, 3000);
//        });
//        adapter.setUpFetchEnable(false);
//        adapter.setUpFetchListener(this::startUpFetch);
    }

    private void startUpFetch() {
        /**
         * set fetching on when start network request.
         */
        adapter.setUpFetching(true);
        /**
         * get data from internet.
         */
        recyclerView.postDelayed(() -> {
            adapter.addData(0, "addd");
            /**
             * set fetching off when network request ends.
             */
            adapter.setUpFetching(false);
        }, 5000);

    }
}
