package com.oleksiykovtun.iwmy.speeddating.android;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.oleksiykovtun.android.cooltools.CoolApplication;
import com.oleksiykovtun.android.cooltools.CoolFormatter;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.Base64Converter;
import com.oleksiykovtun.iwmy.speeddating.BuildConfig;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * Created by alx on 2015-04-05.
 */
public class ImageManager {

    public static Bitmap scaleBitmapToMinSideSize(Bitmap inputBitmap, double minSideSize) {
        double scaleFactor = Math.min(1,
                minSideSize / Math.min(inputBitmap.getWidth(), inputBitmap.getHeight()));
        return Bitmap.createScaledBitmap(inputBitmap,
                (int)(inputBitmap.getWidth() * scaleFactor),
                (int)(inputBitmap.getHeight() * scaleFactor), true);
    }

    public static void setUserPhoto(ImageView userPicImageView, User user) {
        if (! user.getPhoto().isEmpty()) {
            Picasso.with(CoolApplication.getContext()).load(BuildConfig.BACKEND_URL + Api.IMAGES
                    + Api.GET + "/" + user.getPhoto()).into(userPicImageView);
        } else {
            setUserDefaultImage(userPicImageView, user);
        }
    }

    public static void setUserThumbnail(ImageView userPicImageView, User user) {
        if (! user.getThumbnail().isEmpty()) {
            Picasso.with(CoolApplication.getContext()).load(BuildConfig.BACKEND_URL + Api.IMAGES
                    + Api.GET_THUMBNAIL + "/" + user.getThumbnail()).into(userPicImageView);
        } else {
            setUserDefaultImage(userPicImageView, user);
        }
    }

    private static void setUserDefaultImage(ImageView userPicImageView, User user) {
        if (user.getGender().equals(User.MALE)) {
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

    public static String getBase64StringFromBitmap(Bitmap bitmap) {
        String base64image = "";
        if (bitmap != null && bitmap.getHeight() > 10) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            base64image = Base64Converter.getBase64StringFromBytes(stream.toByteArray());
        }
        return base64image;
    }

}
