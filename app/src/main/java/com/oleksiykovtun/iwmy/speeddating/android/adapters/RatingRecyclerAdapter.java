package com.oleksiykovtun.iwmy.speeddating.android.adapters;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
        public TextView usernameTextView;
        public CheckBox selectionCheckBox;
        public EditText commentEditText;

        public ViewHolder(View view) {
            super(view);
            numberTextView = (TextView) view.findViewById(R.id.label_number);
            usernameTextView = (TextView) view.findViewById(R.id.label_rating_username);
            selectionCheckBox = (CheckBox) view.findViewById(R.id.checkbox_selection);
            commentEditText = (EditText) view.findViewById(R.id.input_comment);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_rating_list_item, parent, false));
        viewHolder.selectionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    itemClickListener.onClick(dataSet.get(getPosition(viewHolder)), v);
                } catch (Throwable e) {
                    Log.e("IWMY", "Checkbox toggling failed", e);
                }
            }
        });
        viewHolder.commentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        ((Rating) dataSet.get(getPosition(viewHolder)))
                                .setComment("" + viewHolder.commentEditText.getText());
                        itemClickListener.onClick(dataSet.get(getPosition(viewHolder)), v);
                    } catch(Throwable e){
                        Log.e("IWMY", "Comment editing failed", e);
                    }
                }
            }
        });
        return viewHolder;
    }

    private int getPosition(ViewHolder viewHolder) {
        return Integer.parseInt("" + viewHolder.selectionCheckBox.getTag());
    }

    @Override
    public void onBindViewHolder(CoolRecyclerAdapter.ViewHolder holder, int position) {
        Rating rating = (Rating) (dataSet.get(position));
        ((ViewHolder) holder).numberTextView.setText(rating.getNumber());
        ((ViewHolder) holder).usernameTextView.setText(rating.getUsername());
        ((ViewHolder) holder).selectionCheckBox.setChecked(rating.getSelection()
                .equals(Rating.SELECTED));
        ((ViewHolder) holder).selectionCheckBox.setAlpha(1f);
        ((ViewHolder) holder).selectionCheckBox.setTag(position);
        ((ViewHolder) holder).commentEditText.setText(rating.getComment());
    }

}
