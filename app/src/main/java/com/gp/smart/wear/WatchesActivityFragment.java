package com.gp.smart.wear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gp.smart.wear.Adapters.CardViewsAdapter;
import com.gp.smart.wear.Entities.Watch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WatchesActivityFragment extends Fragment {

    List<Watch> watches = new ArrayList<>();
    String mbrand = "";

    public WatchesActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watches, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.watches_listview);
        final CardViewsAdapter adapter = new CardViewsAdapter(getActivity(), R.layout.watch_card_view, watches);
        listView.setAdapter(adapter);
        Intent intent = getActivity().getIntent();
        mbrand = intent.getStringExtra("brand");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final String MY_TAG = "test";

        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        String brand = mbrand.replace(mbrand.charAt(0),Character.toUpperCase(mbrand.charAt(0)))+ " Watches";
        getActivity().setTitle(brand);
        progressDialog.setTitle(brand);
        progressDialog.setMessage("Please wait while getting watches list");
        progressDialog.show();

        myRef.child("watches").child(mbrand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                watches.clear();
                for (DataSnapshot watchDataSnapshot : dataSnapshot.getChildren()) {
                    Watch watch = watchDataSnapshot.getValue(Watch.class);
                    watches.add(watch);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(MY_TAG, "onCancelled", databaseError.toException());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("watch", watches.get(i));
                intent.putExtra("watch_id", watches.get(i).getId());
                startActivity(intent);
            }
        });
        return view;
    }
}