package com.oleksiykovtun.iwmy.speeddating.android.fragments.common;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.R;
import com.oleksiykovtun.iwmy.speeddating.android.adapters.RatingRecyclerAdapter;
import com.oleksiykovtun.iwmy.speeddating.android.fragments.AppFragment;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alx on 2015-02-12.
 */
public abstract class UserQuestionnaireFragment extends AppFragment {

    protected List<Rating> ratingList = new ArrayList<>();

    protected RatingRecyclerAdapter ratingRecyclerAdapter = new RatingRecyclerAdapter(ratingList);

    protected void setupRecyclerView(RecyclerView ratingRecyclerView) {
        ratingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerItemClickListener(ratingRecyclerAdapter);
        ratingRecyclerView.setAdapter(ratingRecyclerAdapter);
    }

    @Override
    public void onPostReceive(String postTag, List response) {
        switch (postTag) {
            case Api.RATINGS + Api.GET_FOR_ATTENDANCE_ACTIVE:
                ratingList.clear();
                ratingList.addAll(response);
                ratingRecyclerAdapter.notifyDataSetChanged();
                break;
            case Api.RATINGS + Api.PUT_ACTUAL:
                confirm();
                break;
            case Api.RATINGS + Api.PUT:
                if (!response.isEmpty()) {
                    Rating responseRating = (Rating) response.get(0);
                    for (Rating rating : ratingList) {
                        if (rating.getNumber().equals(responseRating.getNumber())) {
                            rating.setSelection(responseRating.getSelection());
                        }
                    }
                }
                ratingRecyclerAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(Serializable objectAtClicked, View view) {
        switch (view.getId()) {
            case R.id.checkbox_selection:
                view.setAlpha(0.5f); // intermediate state
                toggleSelection((Rating) objectAtClicked); // checking the object
                int selectedRatingsCount = 0;
                for (Rating rating : ratingList) {
                    if (rating.getSelection().equals(Rating.SELECTED)) {
                        ++selectedRatingsCount;
                    }
                }
                if (selectedRatingsCount <= getMaxRatingsCount()) {
                    if (!isPostRequestRunningNow()) {
                        post(Api.RATINGS + Api.PUT, Rating[].class, objectAtClicked);
                    } else {
                        toggleSelection((Rating) objectAtClicked); // unchecking the object
                        ratingRecyclerAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(R.string.message_you_cannot_add_more_ratings);
                    toggleSelection((Rating) objectAtClicked); // unchecking the object
                    ratingRecyclerAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.input_comment:
                if (!isPostRequestRunningNow()) {
                    post(Api.RATINGS + Api.PUT, Rating[].class, objectAtClicked);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                sendRating();
                break;
        }
    }

    protected void toggleSelection(Rating rating) {
        rating.setSelection(rating.getSelection().equals(Rating.SELECTED)
                ? Rating.NOT_SELECTED : Rating.SELECTED);
    }

    protected void sendRating() {
        if (!ratingList.isEmpty() && !isPostRequestRunningNow()) {
            post(Api.RATINGS + Api.PUT_ACTUAL, Rating[].class, ratingList.toArray());
        }
    }


    protected abstract void confirm();

    protected abstract int getMaxRatingsCount();

}
