package com.oleksiykovtun.android.cooltools;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

/**
 * Base recycler adapter with support of item click listener delegating
 */
public abstract class CoolRecyclerAdapter extends RecyclerView
        .Adapter<CoolRecyclerAdapter.ViewHolder> {

    private CoolClickListener itemClickListener;

    protected List<Serializable> dataSet;

    public interface CoolClickListener {

        void onClick(Serializable residingObject);

        void onClick(Serializable residingObject, View view);

        void onLongClick(Serializable residingObject);

    }

    protected void setImageFromBase64String(ImageView imageView, String base64String) {
        try {
            if (! base64String.isEmpty()) {
                imageView.setImageBitmap(CoolFormatter.getImageBitmap(base64String));
            }
        } catch (Throwable e) {
            Log.e("IWMY", "Image setting failed", e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null && dataSet != null && dataSet.size() > getPosition()) {
                itemClickListener.onClick(dataSet.get(getPosition()));
                itemClickListener.onClick(dataSet.get(getPosition()), v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemClickListener != null && dataSet != null && dataSet.size() > getPosition()) {
                itemClickListener.onLongClick(dataSet.get(getPosition()));
            }
            return true;
        }
    }

    public CoolRecyclerAdapter(List dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemCount() {
        return (dataSet != null) ? dataSet.size() : 0;
    }

    public void setOnItemClickListener(CoolClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
