package com.jacksonsmolenko.iwmy.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.AboutFragment;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.jacksonsmolenko.iwmy.fragments.ChangePasswordFragment;
import com.jacksonsmolenko.iwmy.fragments.PoliticsFragment;
import com.jacksonsmolenko.iwmy.fragments.RulesFragment;

public class SettingsFragment extends AppFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        registerContainerView(view);

        registerClickListener(R.id.profile);
        registerClickListener(R.id.change_email);
        registerClickListener(R.id.change_password);
        registerClickListener(R.id.version);
        registerClickListener(R.id.rules);
        registerClickListener(R.id.politics);
        registerClickListener(R.id.comment);
        registerClickListener(R.id.exit);
        registerClickListener(R.id.delete_profile);

        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.profile:
                CoolFragmentManager.showAtTop(new UserProfileEditFragment());
                break;
            case R.id.change_email:
                
                break;
            case R.id.change_password:
                CoolFragmentManager.showAtTop(new ChangePasswordFragment());
                break;
            case R.id.version:
                CoolFragmentManager.showAtTop(new AboutFragment());
                break;
            case R.id.rules:
                CoolFragmentManager.showAtTop(new RulesFragment());
                break;
            case R.id.politics:
                CoolFragmentManager.showAtTop(new PoliticsFragment());
                break;
            case R.id.comment:

                break;
            case R.id.exit:

                break;
            case R.id.delete_profile:

                break;
        }
    }
}
