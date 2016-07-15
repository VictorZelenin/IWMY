package com.jacksonsmolenko.iwmy.data_binders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.DataBindAdapter;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by victor on 15.07.16.
 */
public class OthersEventsBinder extends DataBinder<OthersEventsBinder.ViewHolder> {

    private List<Event> eventList;

    public OthersEventsBinder(DataBindAdapter adapter, List<Event> eventList) {
        super(adapter);
        this.eventList = eventList;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_event_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        //
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameAndAgeTextView;
        private TextView dateTextView;
        private TextView descriptionView;
        private ImageView photoImageView;

        public ViewHolder(View view) {
            super(view);

            nameAndAgeTextView = (TextView) view.findViewById(R.id.label_event_organizer_and_place);
            dateTextView = (TextView) view.findViewById(R.id.label_event_time);
            descriptionView = (TextView) view.findViewById(R.id.label_event_description);
            photoImageView = (ImageView) view.findViewById(R.id.event_photo);
        }
    }
}
