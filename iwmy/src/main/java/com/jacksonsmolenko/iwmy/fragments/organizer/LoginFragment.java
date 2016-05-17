package com.jacksonsmolenko.iwmy.fragments.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.jacksonsmolenko.iwmy.fragments.PoliticsFragment;
import com.jacksonsmolenko.iwmy.fragments.RegistrationFragment;
import com.jacksonsmolenko.iwmy.fragments.RestorePasswordFragment;
import com.jacksonsmolenko.iwmy.fragments.RulesFragment;
import com.jacksonsmolenko.iwmy.fragments.user.UserMainActivity;

public class LoginFragment extends AppFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organaizer_login, container, false);
        registerContainerView(view);
        registerClickListener(R.id.button_login);
        registerClickListener(R.id.forgotPassword);
        registerClickListener(R.id.button_registration);
        registerClickListener(R.id.how_to_become_user);
        additionText();
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.button_login:
                Intent intent = new Intent(getActivity(), OrganizerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.forgotPassword:
                CoolFragmentManager.showAtTop(new RestorePasswordFragment());
                break;
            case R.id.button_registration:
                CoolFragmentManager.showAtTop(new RegistrationFragment());
                break;
            case R.id.how_to_become_user:
                CoolFragmentManager.showAtTop(new com.jacksonsmolenko.iwmy.fragments.user.LoginFragment());
                break;
        }
    }

    private void additionText(){
        TextView rules = (TextView) getViewById(R.id.rules);
        SpannableString ss = new SpannableString("Используя приложение IWMY вы соглашаетесь\n\r с условиями использования\n\r и политикой конфиденциальности");
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                CoolFragmentManager.showAtTop(new RulesFragment());
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                CoolFragmentManager.showAtTop(new PoliticsFragment());
            }
        };

        ss.setSpan(span1, 46, 69, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 74, 102, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        rules.setText(ss);
        rules.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
