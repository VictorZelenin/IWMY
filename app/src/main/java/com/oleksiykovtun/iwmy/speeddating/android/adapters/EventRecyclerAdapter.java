package com.oleksiykovtun.iwmy.speeddating.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleksiykovtun.android.cooltools.CoolRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by alx on 2015-02-16.
 */
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
        public TextView placeTextView;
        public TextView timeTextView;
        public ImageView photoImageView;

        public ViewHolder(View view) {
            super(view);
            placeTextView = (TextView) view.findViewById(R.id.label_event_organizer_and_place);
            timeTextView = (TextView) view.findViewById(R.id.label_event_time);
            photoImageView = (ImageView) view.findViewById(R.id.photo);
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
        ((ViewHolder) holder).placeTextView.setText(event.getPlace());
        ((ViewHolder) holder).timeTextView.setText(event.getTime()
                + ((showCity && !event.getCity().isEmpty()) ? (", " + event.getCity()) : "")
                + ((showCity && !event.getCountry().isEmpty()) ? (", " + event.getCountry()) : ""));
        ImageManager.setEventThumbnail(((ViewHolder) holder).photoImageView, event.getThumbnail());
    }

}
