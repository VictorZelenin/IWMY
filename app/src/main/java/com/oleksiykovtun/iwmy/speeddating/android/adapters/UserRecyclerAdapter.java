package com.oleksiykovtun.iwmy.speeddating.android.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleksiykovtun.android.cooltools.CoolRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.TimeConverter;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-16.
 */
public class UserRecyclerAdapter extends CoolRecyclerAdapter {

    private int highlightColor = Color.TRANSPARENT;

    public UserRecyclerAdapter(List dataSet) {
        super(dataSet);
    }

    public UserRecyclerAdapter(List dataSet, int highlightColor) {
        super(dataSet);
        this.highlightColor = highlightColor;
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
        ((View)(((ViewHolder) holder).nameTextView.getParent().getParent())).setBackgroundColor(
                user.getIsChecked().equals("true") ? highlightColor : Color.TRANSPARENT);
        ((ViewHolder) holder).ageTextView.setText(TimeConverter.getYearsFromDate(user
                    .getBirthDate()));
        ((ViewHolder) holder).locationTextView.setText(", " + user.getLocation());
        ImageManager.setUserThumbnail(((ViewHolder) holder).photoImageView, user.getGender(),
                user.getThumbnail());
    }

}
