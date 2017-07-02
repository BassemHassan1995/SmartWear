package com.gp.smart.wear.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by basse on 21-Dec-16.
 */

public class ImageAdapter extends BaseAdapter {

    Context mContext;
    int [] array ;

    public ImageAdapter(Context mContext, int[] array) {
        this.mContext = mContext;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;

        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(15, 5, 15, 5);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageResource(array[i]);
        return imageView;
    }
}
