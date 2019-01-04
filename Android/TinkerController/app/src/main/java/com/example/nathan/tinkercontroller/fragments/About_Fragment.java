package com.example.nathan.tinkercontroller.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nathan.tinkercontroller.R;
import com.example.nathan.tinkercontroller.activities.GamepadActivity;
import com.example.nathan.tinkercontroller.activities.MainActivity;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


public class About_Fragment extends CyaneaFragment {
    private Toolbar toolbar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup the listener for the fragment B
        ((MainActivity) getActivity()).setToolbar(toolbar);
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).setToolbar(null);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        toolbar = v.findViewById(R.id.toolbar);
        View headerView = v.findViewById(R.id.headerBackground);
        ImageView githubCat = v.findViewById(R.id.github_cat);
        ImageView donateBtn = v.findViewById(R.id.donate_button);
        TextView appName = v.findViewById(R.id.ab_title);
        TextView authorName = v.findViewById(R.id.author_name);
        headerView.setBackgroundColor(getCyanea().getPrimaryDark());
        if (getCyanea().isActionBarDark()) {
            appName.setTextColor(Color.WHITE);
            authorName.setTextColor(Color.WHITE);
        } else {
            appName.setTextColor(Color.BLACK);
            authorName.setTextColor(Color.BLACK);
        }
        githubCat.setOnClickListener(v1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link)));
            startActivity(browserIntent);
        });

        donateBtn.setOnClickListener(v1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donateLink)));
            startActivity(browserIntent);
        });

        return v;
    }
}
