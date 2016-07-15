package com.jacksonsmolenko.iwmy.data_binders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.DataBindAdapter;

/**
 * Created by victor on 15.07.16.
 */
public class UpperIconBinder extends DataBinder<UpperIconBinder.ViewHolder> implements View.OnClickListener {

    public UpperIconBinder(DataBindAdapter adapter) {
        super(adapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_event_upper_icons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
//        set up icons
        holder.currentPositionIcon.setOnClickListener(this);
        holder.phoneIcon.setOnClickListener(this);
        holder.likeIcon.setOnClickListener(this);
        holder.settingsIcon.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_position_icon:
                System.out.println("CURRENT POSITION ICON");
                break;
            case R.id.phone_icon:
                System.out.println("PHONE ICON");
                break;

            case R.id.like_icon:
                System.out.println("LIKE ICON");
                break;
            case R.id.settings_icon:
                System.out.println("SETTINGS ICON");
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView currentPositionIcon;
        private ImageView phoneIcon;
        private ImageView likeIcon;
        private ImageView settingsIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            currentPositionIcon = (ImageView) itemView.findViewById(R.id.current_position_icon);
            phoneIcon = (ImageView) itemView.findViewById(R.id.phone_icon);
            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            settingsIcon = (ImageView) itemView.findViewById(R.id.settings_icon);
        }
    }
}
