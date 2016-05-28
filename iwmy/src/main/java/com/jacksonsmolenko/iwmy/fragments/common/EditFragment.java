package com.jacksonsmolenko.iwmy.fragments.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jacksonsmolenko.iwmy.ImageManager;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;

import java.io.File;

public abstract class EditFragment extends AppFragment {

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri photoUri;

    protected Bitmap photoBitmap = null;
    protected Bitmap thumbnailBitmap = null;
    protected boolean photoChanged = false;
    protected String defaultPhotoLink = "";
    protected String defaultThumbnailLink = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_choose_photo:
                openMenu(view);
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
                getImageView(R.id.button_choose_photo).setImageBitmap(photoBitmap);
                getImageView(R.id.button_choose_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoBitmap = ImageManager.rotateRight(photoBitmap);
                        thumbnailBitmap = ImageManager.rotateRight(thumbnailBitmap);
                        getImageView(R.id.button_choose_photo).setImageBitmap(photoBitmap);
                    }
                });
                photoChanged = true;
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
}
