package com.gp.smart.wear.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gp.smart.wear.Entities.Order;
import com.gp.smart.wear.R;

import java.util.List;

/**
 * Created by basse on 22-Jun-17.
 */


public class OrdersListAdapter extends ArrayAdapter<Order> {
    Context context ;
    List<Order> orders;

    public OrdersListAdapter(Context context, int resource, List<Order> orders) {
        super(context, resource, orders);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Nullable
    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.order_list_item, null);
        }
        Order order = getItem(position);

        TextView textView_date = (TextView) view.findViewById(R.id.order_date);
        TextView textView_status = (TextView) view.findViewById(R.id.order_status);
        TextView textView_number = (TextView) view.findViewById(R.id.order_number);
        TextView textView_owner = (TextView) view.findViewById(R.id.order_owner);
        TextView textView_price = (TextView) view.findViewById(R.id.order_price);

        textView_date.setText(order.getDate());
        textView_status.setText(order.getStatus());
        textView_number.setText(order.getNumber());
        textView_owner.setText(order.getOwner());
        textView_price.setText(order.getPrice());

        return view;
    }
}