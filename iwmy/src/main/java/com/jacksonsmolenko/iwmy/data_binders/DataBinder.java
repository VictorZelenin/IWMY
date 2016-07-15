package com.jacksonsmolenko.iwmy.data_binders;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.adapters.DataBindAdapter;

/**
 * Created by victor on 15.07.16.
 */
public abstract class DataBinder<T extends RecyclerView.ViewHolder> {

    private DataBindAdapter adapter;

    public DataBinder(DataBindAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract T newViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder, int position);

    public abstract int getItemCount();
}
