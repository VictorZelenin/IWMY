package com.jacksonsmolenko.iwmy.fragments.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.fragments.AppFragment;
import com.jacksonsmolenko.iwmy.fragments.organizer.OrganizerMainActivity;
import com.jacksonsmolenko.iwmy.fragments.user.UserMainActivity;
import com.oleksiykovtun.iwmy.speeddating.Api;
import com.oleksiykovtun.iwmy.speeddating.data.User;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AuthorizationFragment extends AppFragment {

    private final static int GOOGLE_REQUEST = 9001;
    private final static String TAG = "Auth";
    private String scope = VKScope.EMAIL;
    private GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;

    public void login() {
        String userEmail = getEditText(R.id.input_login_email);
        String password = getEditText(R.id.input_login_password);

        //dev tips
        setText(R.id.input_login_email, "jase@gmail.com");
        setText(R.id.input_login_password, "jase");

        if (!userEmail.isEmpty() && !password.isEmpty()) {
            // todo security

            makeUser(userEmail, password, "", "", "");
        } else {
            showToastLong(R.string.message_no_user_wrong_password);
        }
    }

    public void googleSingIn() {

        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.d(TAG, "startActivity google");
        startActivityForResult(signInIntent, GOOGLE_REQUEST);
    }

    public void vkSingIn() {
        VKSdk.login(getActivity(), scope);

    }

    public void fbSingIn() {

        FacebookSdk.sdkInitialize(getContext());
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        LoginManager lm = LoginManager.getInstance();
        lm.logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
        callbackManager = CallbackManager.Factory.create();
        lm.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "fb success");

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, object.toString());
                                handleSignInResult(object);
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,gender,link,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "fb cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "fb error");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onResult " + requestCode + " " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        //vk
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d(TAG, "vk login" + res.toString());
                String id = res.userId;
                String email = res.email;

            }
            @Override
            public void onError(VKError error) {
        // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        //fb
        if (requestCode == 64206) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            }

        // google
        if (requestCode == GOOGLE_REQUEST) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                if (result.isSuccess()) {

                    handleSignInResult(result);
                } else
                    Toast.makeText(getActivity(), "smth wrong in google" + result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        GoogleSignInAccount acct = result.getSignInAccount();
        String email = acct.getEmail();
        String name = acct.getDisplayName();
        String id = acct.getId();
        /*Uri photo = acct.getPhotoUrl();*/

        makeUser(email, id, name, "", "");
    }

    private void handleSignInResult(JSONObject object){
        try {
            String id = object.getString("id");
            String name = object.getString("name");
            String email = object.getString("email");
            String birthday = object.getString("birthday");
            String gender = object.getString("gender");
            /*String link = object.getString("link");
            String photoURL = object.getString("url");*/

            makeUser(email, id, name, birthday, gender);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void makeUser(String email, String password, String name, String birthday, String gender){
        Log.d(TAG, "singed in " + email + " " + password + " " + name);

        User wildcardLoginUser = new User(email, password, name,
                "", name, "", "", "", birthday, "", "", gender, "", "", "", "", "", "", "", "", "", "",
                "");
        post(Api.USERS + Api.GET_LOGIN, User[].class, wildcardLoginUser);
    }

    @Override
    public void onPostReceive(String tag, List response) {
        switch (tag) {
            case Api.USERS + Api.GET_LOGIN:
                if (response.size() != 1) {
                    showToastLong(R.string.message_no_user_wrong_password);
                } else {
                    Account.saveUser(response.get(0));
                    if (Account.getUser().getGroup().equals(User.ORGANIZER)) {
                        Intent intent = new Intent(getActivity(), OrganizerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), UserMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}

