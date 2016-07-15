package com.jacksonsmolenko.iwmy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jacksonsmolenko.iwmy.data_binders.DataBinder;

/**
 * Created by victor on 15.07.16.
 */
public abstract class DataBindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getDataBinder(viewType).newViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int binderPosition = getBinderPosition(position);
        getDataBinder(holder.getItemViewType()).bindViewHolder(holder, binderPosition);
    }

    @Override
    public abstract int getItemCount();

    @Override
    public abstract int getItemViewType(int position);

    public abstract <T extends DataBinder> T getDataBinder(int viewType);

    public abstract int getPosition(DataBinder binder, int binderPosition);

    public abstract int getBinderPosition(int position);
}
