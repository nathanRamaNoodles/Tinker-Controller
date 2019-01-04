package com.example.nathan.tinkercontroller.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nathan.tinkercontroller.fragments.About_Fragment;
import com.example.nathan.tinkercontroller.fragments.Home_Fragment;
import com.example.nathan.tinkercontroller.R;
import com.google.android.material.navigation.NavigationView;
import com.jaredrummler.cyanea.Cyanea;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsActivity;
import com.jaredrummler.cyanea.prefs.CyaneaTheme;

public class MainActivity extends CyaneaAppCompatActivity {

    //butter-knife stuff
//    @Nullable
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @Nullable
//    @BindView(R.id.fab)
//    FloatingActionButton fab;


    private Toast mToast;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    //fragments, manager
    FragmentTransaction ft;
    private Home_Fragment mHomeFragment = new Home_Fragment();
    private About_Fragment mAboutFragment = new About_Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) menuItem -> {
            int id = menuItem.getItemId();

            switch (id) {
                case R.id.game_controller:
                    ToastMaker("Home Fragment");
                    loadFragment(mHomeFragment);
                    break;
                case R.id.settings:
                    ToastMaker("Settings");
                    startActivity(new Intent(this, CyaneaSettingsActivity.class));
                    break;
                case R.id.about:
                    ToastMaker("Under Construction :)");
                    loadFragment(mAboutFragment);
                    break;
                case R.id.github_cat:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link)));
                    startActivity(browserIntent);
//                default:
//                    ToastMaker(":'(");
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        View header = navigationView.getHeaderView(0);
        ImageView appImage = header.findViewById(R.id.app_image);
        TextView appName = header.findViewById(R.id.app_name);

        if (getCyanea().isActionBarDark()) {
            appName.setTextColor(Color.WHITE);
        } else {
            appName.setTextColor(Color.BLACK);
        }

        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .apply(RequestOptions.circleCropTransform().centerCrop())
                .into(appImage);

        if (null == savedInstanceState) {
            // set your initial fragment object
            navigationView.setCheckedItem(R.id.game_controller);
            loadFragment(mHomeFragment);
        } else {  //On Orientation change, this saves the fragment's state and scroll position
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container); //get old fragments on rotation

            if (frag.getClass().equals(mHomeFragment.getClass())) {
                mHomeFragment = (Home_Fragment) frag;
            } else if (frag.getClass().equals(mAboutFragment.getClass())) {
                mAboutFragment = (About_Fragment) frag;
            }
        }

    }

    //Fragment loader/animator
    private boolean loadFragment(Fragment currentFragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.fragment_container, currentFragment);
        ft.commit();
        return true;
    }

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            if (getCyanea().isActionBarDark()) {//this method takes any toolbar and fixes its color depending on the primary color.
                toolbar.setTitleTextColor(Color.WHITE);
            } else {
                toolbar.setTitleTextColor(Color.BLACK);
            }
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            };

            //Setting the actionbarToggle to drawer layout
//            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(actionBarDrawerToggle);

            //calling sync state is necessary or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();
        } else {
            drawer.addDrawerListener(null);
        }
    }

    private void ToastMaker(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container); //get old fragments on rotation

            if (frag.getClass().equals(mHomeFragment.getClass())) {
                super.onBackPressed();
            } else {
                navigationView.setCheckedItem(R.id.game_controller);
                loadFragment(mHomeFragment);
            }
        }
    }
}
