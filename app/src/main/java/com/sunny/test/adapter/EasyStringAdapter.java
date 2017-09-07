package com.sunny.test.adapter;

import android.support.annotation.Nullable;

import com.sunny.test.R;
import com.sunny.test.base.BaseQuickAdapter;
import com.sunny.test.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Sunny on 2017/9/4 0004.
 */

public class EasyStringAdapter extends BaseQuickAdapter<String, BaseViewHolder>{
    public EasyStringAdapter(@Nullable List<String> data) {
        super(R.layout.easy_string_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text, item);
    }
}
