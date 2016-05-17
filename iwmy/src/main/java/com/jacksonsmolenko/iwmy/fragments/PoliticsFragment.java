package com.jacksonsmolenko.iwmy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacksonsmolenko.iwmy.R;

public class PoliticsFragment extends AppFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_politics, container, false);
        registerContainerView(view);
        return view;
    }
}
