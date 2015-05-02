package com.oleksiykovtun.iwmy.speeddating.android.fragments.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.Account;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.io.File;


/**
 * Created by alx on 2015-02-12.
 */
public abstract class EventEditFragment extends AppFragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    // todo reuse with ProfileEditFragment
    private Uri photoUri;
    private Bitmap photoBitmap = null;
    private Bitmap thumbnailBitmap = null;

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
        switch (view.getId()) {
            case R.id.button_photo:
                if (((Button)view).getText().equals(getText(R.string.button_choose_photo))) {
                    // setting photo
                    openMenu(view);
                } else {
                    // removing photo
                    getImageView(R.id.image_event_pic).setImageResource(R.drawable.no_photo_invisible);
                    photoBitmap = null;
                    thumbnailBitmap = null;
                    ((Button) getViewById(R.id.button_photo)).setText(R.string.button_choose_photo);
                }
                break;
            case R.id.button_select_date:
                openDatePicker();
                break;
            case R.id.button_select_time:
                openTimePicker();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK &&
                (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_FROM_GALLERY)) {
            try {
                if (requestCode == REQUEST_IMAGE_FROM_GALLERY) {
                    photoUri = data.getData();
                }
                Bitmap rawBitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), photoUri);
                photoBitmap = ImageManager.scaleBitmapToMinSideSize(rawBitmap, 640);
                thumbnailBitmap = ImageManager.cropCenterSquare(ImageManager
                        .scaleBitmapToMinSideSize(rawBitmap, 80));
                getImageView(R.id.image_event_pic).setImageBitmap(photoBitmap);
                ((Button) getViewById(R.id.button_photo)).setText(R.string.button_remove_photo);
            } catch (Throwable e) {
                showToast(R.string.message_photo_getting_error);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_from_camera:
                try {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    final String tempPhotoName = "temp_photo.jpg";
                    new File(getActivity().getFilesDir(), tempPhotoName).delete();
                    getActivity().openFileOutput(tempPhotoName, Context.MODE_WORLD_WRITEABLE)
                            .close(); // todo replace
                    photoUri = Uri.fromFile(new File(getActivity().getFilesDir(),
                            tempPhotoName));
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (Throwable e) {
                    showToast(R.string.message_photo_getting_error);
                }
                break;
            case R.id.menu_from_gallery:
                try {
                    Intent selectPictureIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(selectPictureIntent, REQUEST_IMAGE_FROM_GALLERY);
                } catch (Throwable e) {
                    showToast(R.string.message_photo_getting_error);
                }
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.choose_photo, menu);
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
            ImageManager.setEventPhoto(getImageView(R.id.image_event_pic), event.getPhoto());
            ((Button) getViewById(R.id.button_photo)).setText(R.string.button_remove_photo);
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
        String photo = ImageManager.getBase64StringFromBitmap(photoBitmap, 90);
        String thumbnail = ImageManager.getBase64StringFromBitmap(thumbnailBitmap, 85);
        String streetAddress = getEditText(R.id.input_event_address);
        String freePlaces = getEditText(R.id.input_event_places);
        String cost = getEditText(R.id.input_event_cost);
        String description = getEditText(R.id.input_event_description);

        return new Event(Account.getUser().getEmail(), time, place,
                streetAddress, photo, thumbnail, freePlaces, cost, description);
    }

}
