package com.gp.smart.wear;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gp.smart.wear.Adapters.CardViewsAdapter;
import com.gp.smart.wear.Entities.Cart;
import com.gp.smart.wear.Entities.Order;
import com.gp.smart.wear.Entities.User;
import com.gp.smart.wear.Entities.Watch;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class CartActivityFragment extends Fragment {

    private static final String MY_TAG = "test";
    private TextView mPrice;
    private Button mConfirm_button;
    private LinearLayout mTotal_layout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference().child("users");
    DatabaseReference watchesRef = database.getReference().child("watches");
    List<Watch> cartItems = new ArrayList<>();
    CardViewsAdapter adapter;
    Cart cart = new Cart();

    public CartActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mPrice = (TextView) view.findViewById(R.id.price_value);
        mConfirm_button = (Button) view.findViewById(R.id.confirm_button);
        mTotal_layout = (LinearLayout) view.findViewById(R.id.total_layout);
        ListView listView = (ListView) view.findViewById(R.id.cart_listview);
        listView.setEmptyView(view.findViewById(R.id.empty_text));

        adapter = new CardViewsAdapter(getActivity(), R.layout.watch_card_view, cartItems);
        listView.setAdapter(adapter);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserFile", MODE_PRIVATE);
        final String user_id = prefs.getString("user_id", "");
        //Get the cart info from Firebase Database
        getCart(user_id);

        mConfirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersRef.child(user_id).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Cart cart = dataSnapshot.getValue(Cart.class);
                        createNewOrder(user_id, cart);
                        cart.cartWatches_IDs.clear();
                        cart.price = "0$";
                        usersRef.child(user_id).child("cart").setValue(cart);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                cartItems.clear();
                adapter.notifyDataSetChanged();
                getActivity().finish();
            }
        });

        return view;
    }

    private void getCart(final String user_id) {
        usersRef.child(user_id).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cart = dataSnapshot.getValue(Cart.class);
                if (!cart.cartWatches_IDs.isEmpty()){
                    mTotal_layout.setVisibility(View.VISIBLE);
                    mConfirm_button.setVisibility(View.VISIBLE);
                    for (String watch_id : cart.cartWatches_IDs) {
                        getWatch(watch_id);
                    }
                    mPrice.setText(cart.price);
                }
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
                        cartItems.add(watch);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void createNewOrder(final String user_id, Cart cart) {
        final String price = cart.price;
        final List<String> watches_IDs = new ArrayList<>(cart.cartWatches_IDs);

        usersRef.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                List<Order> orders = new ArrayList<>();
                if (user.getOrders() != null) {
                    orders = user.getOrders();
                }
                Order new_order = new Order("22 June 2017", "Delivered", "Order #" + orders.size(), "Bassem Hassan", price, watches_IDs);
                orders.add(new_order);

                usersRef.child(user_id).child("orders").setValue(orders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
