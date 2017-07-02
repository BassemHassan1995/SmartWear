package com.gp.smart.wear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gp.smart.wear.Adapters.CardViewsAdapter;
import com.gp.smart.wear.Entities.Order;
import com.gp.smart.wear.Entities.Watch;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderActivityFragment extends Fragment {

    private TextView mPrice;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference().child("users");
    DatabaseReference watchesRef = database.getReference().child("watches");
    List<Watch> orderItems = new ArrayList<>();
    CardViewsAdapter adapter;
    Order order = new Order();

    public OrderActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mPrice = (TextView) view.findViewById(R.id.price_value);
        ListView listView = (ListView) view.findViewById(R.id.order_listview);
        adapter = new CardViewsAdapter(getActivity(), R.layout.watch_card_view, orderItems);
        listView.setAdapter(adapter);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserFile", MODE_PRIVATE);
        final String user_id = prefs.getString("user_id", "");
        Intent intent = getActivity().getIntent();
        String order_number = intent.getStringExtra("order_number");
        //Get the cart info from Firebase Database
        getOrder(user_id, order_number);

        return view;
    }

    private void getOrder(String user_id, String order_number) {
        usersRef.child(user_id).child("orders").child(order_number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order = dataSnapshot.getValue(Order.class);
                getActivity().setTitle(order.getNumber());

                for (String watch_id : order.getOrderWatches_IDs()) {
                    getWatch(watch_id);
                }
                mPrice.setText(order.getPrice());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Test", "onCancelled", databaseError.toException());
            }
        });


    }

    private void getWatch(final String watch_id) {
        char first_char = watch_id.charAt(0);

        String brand = Utilities.getBrand(first_char);

        watchesRef.child(brand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot watchDataSnapshot : dataSnapshot.getChildren()) {
                    if (watchDataSnapshot.child("id").getValue().equals(watch_id)) {
                        Watch watch = watchDataSnapshot.getValue(Watch.class);
                        orderItems.add(watch);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
