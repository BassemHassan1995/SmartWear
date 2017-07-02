package com.gp.smart.wear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gp.smart.wear.Entities.Cart;
import com.gp.smart.wear.Entities.Watch;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    //Imgur Needed variables
    String Client_ID;
    final String BASE_URL = "https://api.imgur.com/3/album/";
    String ALBUM_ID;
    String YOUR_REQUEST_URL;
    URL imgURL = null;
    Watch my_watch ;
    LinearLayout mygallery;
    ImageView mImageView;
    TextView mOperatingSystem;
    TextView mChipset;
    TextView mCpu;
    TextView mRam;
    TextView mStorage;
    TextView mBattery;
    TextView mSensors;
    TextView mPrice;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users");
    List<String> watches_ids = new ArrayList<>();

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        Client_ID = getString(R.string.client_id);
        Intent intent = getActivity().getIntent();
        my_watch = (Watch) intent.getSerializableExtra("watch");
        ALBUM_ID = my_watch.getAlbum_id();
        YOUR_REQUEST_URL = BASE_URL + ALBUM_ID;

        mygallery = (LinearLayout) view.findViewById(R.id.mygallery);
        mImageView = (ImageView) view.findViewById(R.id.image);

        mOperatingSystem = (TextView) view.findViewById(R.id.os_value);
        mChipset = (TextView) view.findViewById(R.id.chipset_value);
        mCpu = (TextView) view.findViewById(R.id.cpu_value);
        mRam = (TextView) view.findViewById(R.id.ram_value);
        mStorage = (TextView) view.findViewById(R.id.storage_value);
        mBattery = (TextView) view.findViewById(R.id.battery_value);
        mSensors = (TextView) view.findViewById(R.id.sensors_value);
        mPrice = (TextView) view.findViewById(R.id.price_value);

        mOperatingSystem.setText(my_watch.getOperating_system());
        mChipset.setText(my_watch.getChipset());
        mCpu.setText(my_watch.getCpu());
        mRam.setText(my_watch.getRam());
        mStorage.setText(my_watch.getStorage());
        mBattery.setText(my_watch.getBattery());
        mSensors.setText(my_watch.getSensors());
        mPrice.setText(my_watch.getPrice());

        ImagesTask task = new ImagesTask();
        task.execute(YOUR_REQUEST_URL);

        return view;
    }

    private void setimages(List<String> links) {
        String first_link = links.get(0);
        Picasso.with(getContext()).load(first_link).into(mImageView);
        for (int i = 0; i < links.size(); i++) {
            String my_link = links.get(i);
            mygallery.addView(insertPhoto(my_link));
        }
    }

    private View insertPhoto(final String image_link) {

        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LayoutParams(500, 500));
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(0, 10, 0, 10);

        final ImageView imageView = new ImageView(getContext());
        Picasso.with(getContext()).load(image_link).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageDrawable(imageView.getDrawable());
            }
        });

        layout.addView(imageView);
        return layout;
    }

    private class ImagesTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {

            String url = params[0];
            // get your own Id and Secret by registering at https://api.imgur.com/oauth2/addclient
            try {
                imgURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) imgURL.openConnection();
            } catch (IOException e) {
                Log.v("Http Connection", e.toString());
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                Log.v("Request Method", e.toString());
                e.printStackTrace();
            }
            conn.setRequestProperty("Authorization", "Client-ID " + Client_ID);

            BufferedReader bin = null;
            try {
                bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } catch (IOException e) {
                Log.v("Buffered Reader", e.toString());
                e.printStackTrace();
            }

            //below will print out bin
            String line;
            String lines = "";
            try {
                while ((line = bin.readLine()) != null)
                    lines += line + "\n";
                bin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> links = getLinksfromJson(lines);
            return links;
        }

        @Override
        protected void onPostExecute(List<String> links) {
            setimages(links);
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private List<String> getLinksfromJson(String albumJsonString) {
        List<String> links = new ArrayList<>();
        final String DATA = "data";
        final String IMAGES = "images";
        try {
            JSONObject albumJson = new JSONObject(albumJsonString).getJSONObject(DATA);
            JSONArray images = albumJson.getJSONArray(IMAGES);
            for (int i = 0; i < images.length(); i++) {
                JSONObject image = images.getJSONObject(i);
                String image_link = image.getString("link");
                links.add(i, image_link);
            }
            return links;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_to_cart){
            SharedPreferences prefs = getActivity().getSharedPreferences("UserFile", MODE_PRIVATE);
            final String user_id = prefs.getString("user_id", "");
            myRef = myRef.child(user_id).child("cart");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    cart.price = updatePrice(my_watch.getPrice(), cart.price);
                    cart.cartWatches_IDs.add(my_watch.getId());
                    myRef.setValue(cart);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

          Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private String updatePrice(String watch_price, String cart_price) {
        int cart_price_value = Integer.valueOf(cart_price.replace("$", ""));
        int watch_price_value = Integer.valueOf(watch_price.replace("$", ""));

        return cart_price_value + watch_price_value + "$";
    }
}
