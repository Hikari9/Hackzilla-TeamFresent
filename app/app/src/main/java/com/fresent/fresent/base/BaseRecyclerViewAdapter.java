package com.fresent.fresent.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    private List<T> list = new ArrayList<T>();

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clearList() {
        getList().clear();
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        getList().add(item);
        notifyDataSetChanged();
    }

    public T getItem(int index) {
        return getList().get(index);
    }

    public void setItem(int index, T item) {
        getList().set(index, item);
        notifyItemChanged(index);
    }

    protected abstract VH onCreateViewHolder(View view);

    protected abstract int getLayoutId();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(getLayoutId(), parent, false);
        return onCreateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(getList().get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
