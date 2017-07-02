package com.gp.smart.wear.NavigationDrawer_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gp.smart.wear.Adapters.ImageAdapter;
import com.gp.smart.wear.CartActivity;
import com.gp.smart.wear.R;
import com.gp.smart.wear.WatchesActivity;

public class BrandsFragment extends Fragment {

    public BrandsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brands, container, false);
        int asus = R.drawable.asus_logo;
        int huawei = R.drawable.huawei_logo;
        int samsung = R.drawable.samsung_logo;
        int lg = R.drawable.lg_logo;
        int apple = R.drawable.apple_logo;
        int motorola = R.drawable.motorola_logo;

        int[] brands_logos = new int[]{huawei,lg, samsung,asus, apple,motorola};
        final String[] brands_names = new String[]{"huawei", "lg" ,"samsung" ,"asus", "apple" , "motorola"};
        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(getActivity(), brands_logos));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity() , WatchesActivity.class);
                intent.putExtra("brand" , brands_names[i]);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title_english for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.watches);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_brands, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart){
            Intent intent = new Intent(getActivity() , CartActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
