package com.jacksonsmolenko.iwmy.data_binders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.DataBindAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

/**
 * Created by victor on 15.07.16.
 */
public class MainEventBinder extends DataBinder<MainEventBinder.ViewHolder> implements View.OnClickListener {

    private Event mainEvent;

    public MainEventBinder(DataBindAdapter adapter, Event mainEvent) {
        super(adapter);
        this.mainEvent = mainEvent;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_event_details, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        // set up components using mainEvent obj (photo, TextViews)

        holder.acceptButton.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_button:
                // functionality of accept_button!
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView eventPhoto;

        private TextView eventHeader;
        private TextView eventInfo;
        private TextView eventDescription;

        private Button acceptButton;

        public ViewHolder(View itemView) {
            super(itemView);

            eventPhoto = (ImageView) itemView.findViewById(R.id.main_event_photo);

            eventHeader = (TextView) itemView.findViewById(R.id.event_header_text);
            eventInfo = (TextView) itemView.findViewById(R.id.event_info_text);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description_text);

            acceptButton = (Button) itemView.findViewById(R.id.accept_button);
        }
    }
}
