package com.jacksonsmolenko.iwmy;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.jacksonsmolenko.iwmy.cooltools.CoolApplication;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.Base64Converter;
import com.oleksiykovtun.iwmy.speeddating.data.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ImageManager {

    public static Bitmap rotateRight(Bitmap inputBitmap) {
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.postRotate(90);
        return Bitmap.createBitmap(inputBitmap, 0, 0, inputBitmap.getWidth(),
                inputBitmap.getHeight(), rotationMatrix, true);
    }

    public static Bitmap cropCenterSquare(Bitmap inputBitmap) {
        if (inputBitmap.getWidth() >= inputBitmap.getHeight()){
            return Bitmap.createBitmap(inputBitmap,
                    inputBitmap.getWidth() / 2 - inputBitmap.getHeight() / 2,
                    0, inputBitmap.getHeight(), inputBitmap.getHeight());
        } else {
            return Bitmap.createBitmap(inputBitmap, 0,
                    inputBitmap.getHeight() / 2 - inputBitmap.getWidth() / 2,
                    inputBitmap.getWidth(), inputBitmap.getWidth());
        }
    }

    public static Bitmap scaleBitmapToMinSideSize(Bitmap inputBitmap, double minSideSize) {
        double scaleFactor = Math.min(1,
                minSideSize / Math.min(inputBitmap.getWidth(), inputBitmap.getHeight()));
        return Bitmap.createScaledBitmap(inputBitmap,
                (int)(inputBitmap.getWidth() * scaleFactor),
                (int)(inputBitmap.getHeight() * scaleFactor), true);
    }

    public static void setUserPhoto(ImageView userPicImageView, String userGender,
                                    String userPhoto) {
        if (! userPhoto.isEmpty()) {
            Picasso.with(CoolApplication.getContext()).load(BuildConfig.BACKEND_URL + Api.IMAGES
                    + Api.GET + "/" + userPhoto).into(userPicImageView);
        } else {
            setUserDefaultImage(userPicImageView, userGender);
        }
    }

    public static void setUserThumbnail(ImageView userPicImageView, String userGender,
                                        String userThumbnail) {
        if (! userThumbnail.isEmpty()) {
            Picasso.with(CoolApplication.getContext()).load(BuildConfig.BACKEND_URL + Api.IMAGES
                    + Api.GET_THUMBNAIL + "/" + userThumbnail).into(userPicImageView);
        } else {
            setUserDefaultImage(userPicImageView, userGender);
        }
    }

    private static void setUserDefaultImage(ImageView userPicImageView, String userGender) {
        if (userGender.equals(User.MALE)) {
            userPicImageView.setImageDrawable(CoolApplication.getContext().getResources()
                    .getDrawable(R.drawable.no_photo));
        } else if (userGender.equals(User.FEMALE)) {
            userPicImageView.setImageDrawable(CoolApplication.getContext().getResources()
                    .getDrawable(R.drawable.no_photo));
        }
    }

    public static void setEventPhoto(ImageView eventPicImageView, String eventPhoto) {
        if (! eventPhoto.isEmpty()) {
            Picasso.with(CoolApplication.getContext()).load(BuildConfig.BACKEND_URL + Api.IMAGES
                    + Api.GET + "/" + eventPhoto).into(eventPicImageView);
        } else {
            setEventDefaultImage(eventPicImageView);
        }
    }

    public static void setEventThumbnail(ImageView eventPicImageView, String eventThumbnail) {
        if (! eventThumbnail.isEmpty()) {
            Picasso.with(CoolApplication.getContext()).load(BuildConfig.BACKEND_URL + Api.IMAGES
                    + Api.GET_THUMBNAIL + "/" + eventThumbnail).into(eventPicImageView);
        } else {
            setEventDefaultImage(eventPicImageView);
        }
    }

    private static void setEventDefaultImage(ImageView eventPicImageView) {
        eventPicImageView.setImageDrawable(CoolApplication.getContext().getResources()
                .getDrawable(R.drawable.no_photo));
    }

    public static String getBase64StringFromBitmap(Bitmap bitmap, int quality) {
        String base64image = "";
        if (bitmap != null && bitmap.getHeight() > 10) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            base64image = Base64Converter.getBase64StringFromBytes(stream.toByteArray());
        }
        return base64image;
    }

}
