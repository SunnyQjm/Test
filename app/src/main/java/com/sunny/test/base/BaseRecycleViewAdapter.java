package com.sunny.test.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Sunny on 2017/5/13 0013.
 */

public class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> mList;

    public BaseRecycleViewAdapter(Context context, List<T> mList) {
        this.context = context;
        this.mList = mList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
