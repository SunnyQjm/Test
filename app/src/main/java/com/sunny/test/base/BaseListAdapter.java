package com.sunny.test.base;

import android.support.annotation.Nullable;

import com.sunny.test.R;

import java.util.List;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

public class BaseListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder>{
    public BaseListAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseListAdapter(@Nullable List<T> data) {
        this(R.layout.item_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        helper.setText(R.id.text, item.toString());
    }
}
