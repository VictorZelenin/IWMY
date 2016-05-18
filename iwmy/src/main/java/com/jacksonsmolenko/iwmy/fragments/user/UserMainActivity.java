package com.jacksonsmolenko.iwmy.fragments.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jacksonsmolenko.iwmy.Account;
import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.cooltools.CoolFragmentManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.oleksiykovtun.iwmy.speeddating.data.User;

public class UserMainActivity extends FragmentActivity{

    private Drawer drawerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        CoolFragmentManager.setup(this, R.id.fragment_holder_user);

        CoolFragmentManager.showAtBottom(new EventListFragment()); // show the first fragment

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        initDrawer(toolbar);
    }

    @Override
    public void onBackPressed() {
        if(drawerResult != null && drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }

        if (CoolFragmentManager.isNotEmpty()) {
            CoolFragmentManager.showPrevious(); // pop the top fragment from the stack
        }
        // If there are no fragments in the stack to show, go exit from the app
        if (! CoolFragmentManager.isNotEmpty()) {
            super.onBackPressed();
        }
    }

    private void initDrawer(Toolbar toolbar){
        AccountHeader accountHeader = createAccountHeader();

        drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(initDrawerItems())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 1:
                                CoolFragmentManager.showAtBottom(new EventListFragment());
                                break;
                            case 2:

                                break;
                            case 3:

                                break;
                            case 4:

                                break;
                            case 5:

                                break;
                            case 6:
                                CoolFragmentManager.showAtBottom(new ProfileEditFragment());
                                break;
                            case 7:

                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    @NonNull
    private IDrawerItem[] initDrawerItems() {
        return new IDrawerItem[]{
                new PrimaryDrawerItem().withName(R.string.drawer_item_all_datings),
                new PrimaryDrawerItem().withName(R.string.drawer_item_my_datings),
                new PrimaryDrawerItem().withName(R.string.drawer_item_favorites),
                new PrimaryDrawerItem().withName(R.string.drawer_item_previous_datings),
                new PrimaryDrawerItem().withName(R.string.drawer_item_couples),
                new PrimaryDrawerItem().withName(R.string.drawer_item_profile),
                new PrimaryDrawerItem().withName(R.string.drawer_item_config)};
    }


    private AccountHeader createAccountHeader(){
        ProfileDrawerItem profile = new ProfileDrawerItem()
                .withName(Account.getUser().getNameAndSurname())
                .withEmail(Account.getUser().getBirthDate())
                .withIcon(R.drawable.no_photo);

        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(profile)
                .build();
    }
}
