package com.oleksiykovtun.iwmy.speeddating.android.fragments.common;

import android.view.View;
import android.widget.Button;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.data.Event;


/**
 * Created by alx on 2015-02-12.
 */
public abstract class EventEditFragment extends EditFragment {

    @Override
    public void onDateSet(String dateString) {
        setText(R.id.label_event_date, dateString);
    }

    @Override
    public void onTimeSet(String timeString) {
        setText(R.id.label_event_time, timeString);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_select_date:
                openDatePicker();
                break;
            case R.id.button_select_time:
                openTimePicker();
                break;
        }
    }


    protected void fillForms(Event event) {
        if (event.getTime().contains(" ")) {
            setText(R.id.label_event_date, event.getTime().split(" ")[0]);
            setText(R.id.label_event_time, event.getTime().split(" ")[1]);
        }
        setText(R.id.input_event_place, event.getPlace());
        setText(R.id.input_event_address, event.getStreetAddress());
        setText(R.id.input_event_places, event.getFreePlaces());
        setText(R.id.input_event_cost, event.getCost());
        setText(R.id.input_event_description, event.getDescription());

        if (! event.getPhoto().isEmpty()) {
            ImageManager.setEventPhoto(getImageView(R.id.photo), event.getPhoto());
            ((Button) getViewById(R.id.button_photo)).setText(R.string.button_remove_photo);
            defaultPhotoLink = event.getPhoto();
            defaultThumbnailLink = event.getThumbnail();
        }
    }

    protected boolean checkEvent(Event event) {
        return CoolFormatter.isDateTimeValid(event.getTime())
                && event.getOrganizerEmail().contains("@");
    }

    protected Event makeEvent() {
        String time = getLabelText(R.id.label_event_date) + " "
                + getLabelText(R.id.label_event_time);
        String place = getEditText(R.id.input_event_place);
        String photo = photoChanged
                ? ImageManager.getBase64StringFromBitmap(photoBitmap, 90) : defaultPhotoLink;
        String thumbnail = photoChanged
                ? ImageManager.getBase64StringFromBitmap(thumbnailBitmap, 85) : defaultThumbnailLink;
        String streetAddress = getEditText(R.id.input_event_address);
        String freePlaces = getEditText(R.id.input_event_places);
        String cost = getEditText(R.id.input_event_cost);
        String description = getEditText(R.id.input_event_description);

        return new Event(Account.getUser().getEmail(), time, place,
                streetAddress, photo, thumbnail, freePlaces, cost, description);
    }

}
