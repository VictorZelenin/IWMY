package com.jacksonsmolenko.iwmy.data_binders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.DataBindAdapter;

/**
 * Created by victor on 15.07.16.
 */
public class EventAboutInfoBinder extends DataBinder<EventAboutInfoBinder.ViewHolder> {

    public EventAboutInfoBinder(DataBindAdapter adapter) {
        super(adapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_event_about, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        // set up info (icons and text data)
        // no clicks?
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView locationIcon;
        private ImageView organizerIcon;
        private ImageView invitationIcon;
        private ImageView letterIcon;

        private TextView loactionText;
        private TextView organizerText;
        private TextView invitationText;
        private TextView letterText;

        public ViewHolder(View itemView) {
            super(itemView);

            locationIcon = (ImageView) itemView.findViewById(R.id.location_icon);
            organizerIcon = (ImageView) itemView.findViewById(R.id.organizer_icon);
            invitationIcon = (ImageView) itemView.findViewById(R.id.invitation_icon);
            letterIcon = (ImageView) itemView.findViewById(R.id.letter_icon);

            loactionText = (TextView) itemView.findViewById(R.id.location_text);
            organizerText = (TextView) itemView.findViewById(R.id.organizer_text);
            invitationText = (TextView) itemView.findViewById(R.id.invitation_text);
            letterText = (TextView) itemView.findViewById(R.id.letter_text);
        }
    }
}
