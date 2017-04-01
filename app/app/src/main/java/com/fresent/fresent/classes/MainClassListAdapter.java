package com.fresent.fresent.classes;

import android.view.View;

import com.fresent.fresent.R;
import com.fresent.fresent.base.BaseRecyclerViewAdapter;
import com.fresent.fresent.models.ClassEntity;


public class MainClassListAdapter extends BaseRecyclerViewAdapter<ClassEntity, MainClassListViewHolder> {

    @Override
    protected MainClassListViewHolder onCreateViewHolder(View view) {
        return new MainClassListViewHolder(view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_class_list;
    }
}
