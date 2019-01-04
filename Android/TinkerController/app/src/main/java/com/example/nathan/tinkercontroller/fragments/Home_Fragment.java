package com.example.nathan.tinkercontroller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nathan.tinkercontroller.R;
import com.example.nathan.tinkercontroller.activities.GamepadActivity;
import com.example.nathan.tinkercontroller.activities.MainActivity;
import com.jaredrummler.cyanea.app.CyaneaFragment;
import com.jaredrummler.cyanea.prefs.CyaneaSettingsActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class Home_Fragment extends Fragment {

    private Toolbar toolbar;

    @Override //Fragment Communication Software
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = v.findViewById(R.id.toolbar);
        CardView mCardView = v.findViewById(R.id.Gamepad_Card);

        mCardView.setOnClickListener(v1 -> {
            Intent myIntent = new Intent(getActivity(), GamepadActivity.class);
            startActivity(myIntent);
        });

//        FloatingActionButton fab = v.findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(getView(), "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), CyaneaSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
