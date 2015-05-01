package com.oleksiykovtun.iwmy.speeddating.android;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.oleksiykovtun.android.cooltools.CoolApplication;
import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.Base64Converter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.io.ByteArrayOutputStream;

/**
 * Created by alx on 2015-04-05.
 */
public class ImageManager {

    public static void setUserPic(ImageView userPicImageView, User user) {
        if (! user.getPhoto().isEmpty()) {
            setImageFromBase64String(userPicImageView, user.getPhoto());
        } else if (user.getGender().equals(User.MALE)) {
            userPicImageView.setImageDrawable(CoolApplication.getContext().getResources()
                    .getDrawable(R.drawable.no_photo_man));
        } else if (user.getGender().equals(User.FEMALE)) {
            userPicImageView.setImageDrawable(CoolApplication.getContext().getResources()
                    .getDrawable(R.drawable.no_photo_woman));
        }
    }

    public static void setEventPic(ImageView eventPicImageView, Event event) {
        if (! event.getPhoto().isEmpty()) {
            setImageFromBase64String(eventPicImageView, event.getPhoto());
        }
    }


    public static void setImageFromBase64String(ImageView imageView, String base64String) {
        try {
            if (!base64String.isEmpty()) {
                imageView.setImageBitmap(CoolFormatter.getImageBitmap(base64String));
            }
        } catch (Throwable e) {
            Log.e("IWMY", "Image setting failed", e);
        }
    }

    public static String getBase64StringFromImage(ImageView imageView) {
        String base64photo = "";
        Bitmap photoBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        if (photoBitmap != null && photoBitmap.getHeight() > 10) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
            base64photo = Base64Converter.getBase64StringFromBytes(stream.toByteArray());
        }
        return base64photo;
    }

}
