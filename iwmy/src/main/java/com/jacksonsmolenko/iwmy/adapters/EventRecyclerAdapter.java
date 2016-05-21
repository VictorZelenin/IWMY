package com.jacksonsmolenko.iwmy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.ImageManager;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

public class EventRecyclerAdapter extends CoolRecyclerAdapter {

    private boolean showCity = false;

    public EventRecyclerAdapter(List dataSet) {
        super(dataSet);
    }

    public EventRecyclerAdapter(List dataSet, boolean showCity) {
        super(dataSet);
        this.showCity = showCity;
    }

    public class ViewHolder extends CoolRecyclerAdapter.ViewHolder {
        public TextView nameAndAgeTextView;
        public TextView dateTextView;
        public TextView descriptionView;
        public ImageView photoImageView;

        public ViewHolder(View view) {
            super(view);
            nameAndAgeTextView = (TextView) view.findViewById(R.id.label_event_organizer_and_place);
            dateTextView = (TextView) view.findViewById(R.id.label_event_time);
            descriptionView = (TextView) view.findViewById(R.id.label_event_description);
            photoImageView = (ImageView) view.findViewById(R.id.event_photo);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_event_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CoolRecyclerAdapter.ViewHolder holder, int position) {
        Event event = (Event) (dataSet.get(position));
        ((ViewHolder) holder).nameAndAgeTextView.setText(event.getPlace() + " ("
                + event.getMinAllowedAge() + " - " + event.getMaxAllowedAge() + ")");
        ((ViewHolder) holder).dateTextView.setText(event.getTime()
                + ((showCity && !event.getCity().isEmpty()) ? (" / " + event.getCity()) : ""));
        ((ViewHolder) holder).descriptionView.setText(event.getDescription());
        ImageManager.setEventThumbnail(((ViewHolder) holder).photoImageView, event.getThumbnail());
    }

}
