package com.jacksonsmolenko.iwmy.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.R;

public class TutorialPageFragment extends Fragment{

    private int pageNumber;

    public static TutorialPageFragment newInstance(int page) {
        TutorialPageFragment fragment = new TutorialPageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.tutorial_page, container, false);
        TextView pageHeader = (TextView)result.findViewById(R.id.label_tutorial_page);
        ImageView pageImage = (ImageView)result.findViewById(R.id.image_tutuorial_page);

        int[] images = new int[]{R.drawable.j, R.drawable.f, R.drawable.i};
        String[] pagerText = getResources().getStringArray(R.array.tutorial_text);

        pageImage.setImageResource(images[pageNumber]);
        pageHeader.setText(pagerText[pageNumber]);
        return result;
    }
}
