package com.jacksonsmolenko.iwmy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.R;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.List;

/**
 * Created by victor on 19.07.16.
 */
public class EventDetailsRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int QUANTITY_OF_STATIC_VIEWS = 4;

    private static final int UPPER_ICONS = 0;
    private static final int MAIN_EVENT = 1;
    private static final int ABOUT_INFO = 2;
    private static final int PHOTOS_PANEL = 3;

    private List<Event> eventList;

    public EventDetailsRecycleAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case UPPER_ICONS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event_upper_icons,
                        parent, false);
                return new UpperIconsHolder(view);
            case MAIN_EVENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.element_event_details, parent, false);
                return new MainEventHolder(view);
            case ABOUT_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event_about,
                        parent, false);
                return new AboutInfoHolder(view);
            case PHOTOS_PANEL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_event_photos,
                        parent, false);
                return new PhotosPanelHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_list_item,
                        parent, false);
                return new BasicEventHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case UPPER_ICONS:

                break;
            case MAIN_EVENT:

                break;

            case ABOUT_INFO:

                break;

            case PHOTOS_PANEL:

                break;

            default:

                break;
        }
    }

    @Override
    public int getItemCount() {
        int listSize = eventList == null ? 0 : eventList.size();
        return listSize + QUANTITY_OF_STATIC_VIEWS;
    }

    static public class UpperIconsHolder extends RecyclerView.ViewHolder {
        private ImageView currentPositionIcon;
        private ImageView phoneIcon;
        private ImageView likeIcon;
        private ImageView settingsIcon;

        public UpperIconsHolder(View itemView) {
            super(itemView);

            currentPositionIcon = (ImageView) itemView.findViewById(R.id.current_position_icon);
            phoneIcon = (ImageView) itemView.findViewById(R.id.phone_icon);
            likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
            settingsIcon = (ImageView) itemView.findViewById(R.id.settings_icon);
        }
    }

    public int getItemViewType(int position) {
        return position;
    }

    static public class MainEventHolder extends RecyclerView.ViewHolder {
        private ImageView eventPhoto;

        private TextView eventHeader;
        private TextView eventInfo;
        private TextView eventDescription;

        private Button acceptButton;

        public MainEventHolder(View itemView) {
            super(itemView);

            eventPhoto = (ImageView) itemView.findViewById(R.id.main_event_photo);

            eventHeader = (TextView) itemView.findViewById(R.id.event_header_text);
            eventInfo = (TextView) itemView.findViewById(R.id.event_info_text);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description_text);
            acceptButton = (Button) itemView.findViewById(R.id.accept_button);
        }
    }

    static public class AboutInfoHolder extends RecyclerView.ViewHolder {

        private ImageView locationIcon;
        private ImageView organizerIcon;
        private ImageView invitationIcon;
        private ImageView letterIcon;

        private TextView loactionText;
        private TextView organizerText;
        private TextView invitationText;
        private TextView letterText;

        public AboutInfoHolder(View itemView) {
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

    static public class PhotosPanelHolder extends RecyclerView.ViewHolder {

        private ImageView photo1;
        private ImageView photo2;
        private ImageView photo3;
        private ImageView photo4;
        private ImageView photo5;


        public PhotosPanelHolder(View itemView) {
            super(itemView);

            photo1 = (ImageView) itemView.findViewById(R.id.photo_1);
            photo2 = (ImageView) itemView.findViewById(R.id.photo_2);
            photo3 = (ImageView) itemView.findViewById(R.id.photo_3);
            photo4 = (ImageView) itemView.findViewById(R.id.photo_4);
            photo5 = (ImageView) itemView.findViewById(R.id.photo_5);
        }
    }

    static public class BasicEventHolder extends RecyclerView.ViewHolder {
        private TextView nameAndAgeTextView;
        private TextView dateTextView;
        private TextView descriptionView;
        private ImageView photoImageView;

        public BasicEventHolder(View view) {
            super(view);
            nameAndAgeTextView = (TextView) view.findViewById(R.id.label_event_organizer_and_place);
            dateTextView = (TextView) view.findViewById(R.id.label_event_time);
            descriptionView = (TextView) view.findViewById(R.id.label_event_description);
            photoImageView = (ImageView) view.findViewById(R.id.event_photo);
        }
    }
}



