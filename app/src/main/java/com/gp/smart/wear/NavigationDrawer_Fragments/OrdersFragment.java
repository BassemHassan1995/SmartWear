package com.gp.smart.wear.NavigationDrawer_Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gp.smart.wear.Adapters.OrdersListAdapter;
import com.gp.smart.wear.Entities.Order;
import com.gp.smart.wear.OrderActivity;
import com.gp.smart.wear.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by basse on 18-Jun-17.
 */

public class OrdersFragment extends Fragment{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference().child("users");

    List<Order> orders = new ArrayList<>();
    OrdersListAdapter adapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ListView listView = (ListView) view.findViewById(R.id.orders_listview);
        listView.setEmptyView(view.findViewById(R.id.empty_text));
        adapter = new OrdersListAdapter(getContext(),R.layout.order_list_item,orders);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity() , OrderActivity.class);
                intent.putExtra("order_number" , String.valueOf(position));
                startActivity(intent);
            }
        });
        SharedPreferences prefs = getActivity().getSharedPreferences("UserFile", MODE_PRIVATE);
        String user_id = prefs.getString("user_id", "");

        getOrders (user_id);

        return view;
    }

    private void getOrders(String user_id) {
        orders.clear();
        usersRef.child(user_id).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()){
                    orders.add(orderSnapshot.getValue(Order.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title_english for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.my_orders);
    }
}
