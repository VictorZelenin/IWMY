package com.oleksiykovtun.iwmy.speeddating.android.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleksiykovtun.android.cooltools.CoolRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.TimeConverter;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.text.ParseException;
import java.util.List;

/**
 * Created by alx on 2015-02-16.
 */
public class UserRecyclerAdapter extends CoolRecyclerAdapter {

    public UserRecyclerAdapter(List dataSet) {
        super(dataSet);
    }

    public class ViewHolder extends CoolRecyclerAdapter.ViewHolder {
        public TextView nameTextView;
        public TextView ageTextView;
        public TextView locationTextView;
        public ImageView photoImageView;

        public ViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.label_user_name);
            ageTextView = (TextView) view.findViewById(R.id.label_user_age);
            locationTextView = (TextView) view.findViewById(R.id.label_user_location);
            photoImageView = (ImageView) view.findViewById(R.id.image_user_pic);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CoolRecyclerAdapter.ViewHolder holder, int position) {
        User user = (User) (dataSet.get(position));
        ((ViewHolder) holder).nameTextView.setText(user.getNameAndSurname() + ", ");
        try {
            ((ViewHolder) holder).ageTextView.setText(TimeConverter.getYearsFromDate(user
                    .getBirthDate()));
        } catch (ParseException e) {
            Log.e("IWMY", "Time conversion error", e);
        }
        ((ViewHolder) holder).locationTextView.setText(", " + user.getLocation());
        setImageFromBase64String(((ViewHolder) holder).photoImageView, user.getPhotoBase64());
    }

}
