package com.oleksiykovtun.iwmy.speeddating.android.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.oleksiykovtun.android.cooltools.CoolRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

import java.util.List;

/**
 * Created by alx on 2015-02-16.
 */
public class RatingRecyclerAdapter extends CoolRecyclerAdapter {

    public RatingRecyclerAdapter(List dataSet) {
        super(dataSet);
    }

    public class ViewHolder extends CoolRecyclerAdapter.ViewHolder {
        public TextView numberTextView;
        public CheckBox selectionTextView;
        public TextView commentTextView;

        public ViewHolder(View view) {
            super(view);
            numberTextView = (TextView)view.findViewById(R.id.label_number);
            selectionTextView = (CheckBox)view.findViewById(R.id.checkbox_selection);
            commentTextView = (TextView)view.findViewById(R.id.label_comment);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_rating_list_item, parent, false));
        viewHolder.selectionTextView.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    int position = Integer.parseInt("" + viewHolder.selectionTextView.getTag());
                    ((Rating) dataSet.get(position)).setSelection(isChecked ? "selected" : "");
                } catch (Throwable e) {
                    Log.d("IWMY", "Checkbox toggling failed");
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CoolRecyclerAdapter.ViewHolder holder, int position) {
        Rating rating = (Rating)(dataSet.get(position));
        ((ViewHolder)holder).numberTextView.setText(rating.getNumber());
        ((ViewHolder)holder).selectionTextView.setChecked(! rating.getSelection().isEmpty());
        ((ViewHolder)holder).selectionTextView.setTag(Integer.valueOf(position));
        ((ViewHolder)holder).commentTextView.setText(rating.getComment());
    }

}
