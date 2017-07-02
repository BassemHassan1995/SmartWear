package com.gp.smart.wear.NavigationDrawer_Fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gp.smart.wear.BuildConfig;
import com.gp.smart.wear.R;

/**
 * Created by basse on 16-Jun-17.
 */

public class AboutFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView version = (TextView) view.findViewById(R.id.version_name);
        version.setText("v" + BuildConfig.VERSION_NAME);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title_english for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.about);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_about, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.check_app) + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}