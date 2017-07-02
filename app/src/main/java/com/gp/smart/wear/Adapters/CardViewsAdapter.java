package com.gp.smart.wear.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.smart.wear.R;
import com.gp.smart.wear.Entities.Watch;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by basse on 22-Dec-16.
 */

public class CardViewsAdapter extends ArrayAdapter<Watch> {

    Context context;
    List<Watch> watches;

    public CardViewsAdapter(Context context, int resource, List<Watch> watches) {
        super(context, resource, watches);
        this.context = context;
        this.watches = watches;
    }

    @Nullable
    @Override
    public Watch getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.watch_card_view,null);
        }
        Watch watch = getItem(position);
        ImageView image = (ImageView) view.findViewById(R.id.card_image);
        TextView title  = (TextView) view.findViewById(R.id.card_title);
        TextView description  = (TextView) view.findViewById(R.id.card_description);
        TextView price = (TextView) view.findViewById(R.id.watch_price);

        if (watch.getPhoto()!= null)
            Picasso.with(context).load(watch.getPhoto()).into(image);
        else
            image.setImageResource(R.drawable.img_not_found);

        title.setText(watch.getTitle());
        description.setText(watch.getDescription());
        price.setText(watch.getPrice());

        return view;
    }
}