package com.oleksiykovtun.iwmy.speeddating.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleksiykovtun.android.cooltools.CoolRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.Time;
import com.oleksiykovtun.iwmy.speeddating.android.ImageManager;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.List;

/**
 * Created by alx on 2015-02-16.
 */
public class CoupleRecyclerAdapter extends CoolRecyclerAdapter {

    public CoupleRecyclerAdapter(List dataSet) {
        super(dataSet);
    }

    public class ViewHolder extends CoolRecyclerAdapter.ViewHolder {
        public TextView nameTextView1;
        public TextView ageTextView1;
        public ImageView photoImageView1;
        public TextView nameTextView2;
        public TextView ageTextView2;
        public ImageView photoImageView2;

        public ViewHolder(View view) {
            super(view);
            nameTextView1 = (TextView) view.findViewById(R.id.label_user_name_1);
            ageTextView1 = (TextView) view.findViewById(R.id.label_user_age_1);
            photoImageView1 = (ImageView) view.findViewById(R.id.image_user_pic_1);
            nameTextView2 = (TextView) view.findViewById(R.id.label_user_name_2);
            ageTextView2 = (TextView) view.findViewById(R.id.label_user_age_2);
            photoImageView2 = (ImageView) view.findViewById(R.id.image_user_pic_2);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_couple_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CoolRecyclerAdapter.ViewHolder holder, int position) {
        Couple couple = (Couple) (dataSet.get(position));
        ((ViewHolder) holder).nameTextView1.setText(couple.getName1());
        ((ViewHolder) holder).nameTextView2.setText(couple.getName2());
        ((ViewHolder) holder).ageTextView1.setText(Time.getYearsFromDate(couple
                .getBirthDate1()));
        ((ViewHolder) holder).ageTextView2.setText(Time.getYearsFromDate(couple
                .getBirthDate2()));
        ImageManager.setUserThumbnail(((ViewHolder) holder).photoImageView1, User.MALE,
                couple.getThumbnail1());
        ImageManager.setUserThumbnail(((ViewHolder) holder).photoImageView2, User.FEMALE,
                couple.getThumbnail2());
    }

}
