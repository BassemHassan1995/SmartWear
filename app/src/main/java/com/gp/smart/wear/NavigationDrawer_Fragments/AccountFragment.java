package com.gp.smart.wear.NavigationDrawer_Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gp.smart.wear.Entities.User;
import com.gp.smart.wear.R;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by basse on 18-Jun-17.
 */

public class AccountFragment extends Fragment {
    private EditText mFirstNameField;
    private EditText mLastNameField;
//    private EditText mEmailField;
    private EditText mMobileNumberField;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users");
    User user = new User();
    String user_id = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Views
        mFirstNameField = (EditText) view.findViewById(R.id.input_first_name);
        mLastNameField = (EditText) view.findViewById(R.id.input_last_name);
        mMobileNumberField = (EditText) view.findViewById(R.id.input_mobile);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserFile", MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");

        //Get the user info from Firebase Database
        setUser (user_id);

        return view;
    }

    private void updateUser() {
        String first_name = user.getFirst_name();
        String last_name = user.getLast_name();
        String display_name = first_name + " " + last_name;
        mFirstNameField.setText(first_name);
        mLastNameField.setText(last_name);
        FirebaseUser firebase_user= FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(getContext(), display_name, Toast.LENGTH_SHORT).show();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(display_name)
                .build();

        firebase_user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

//        mEmailField.setText(user.getEmail());
        mMobileNumberField.setText(user.getMobile_number());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title_english for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.account);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done){
            user.first_name = mFirstNameField.getText().toString();
            user.last_name = mLastNameField.getText().toString();
            user.mobile_number = mMobileNumberField.getText().toString();
            myRef.child(user_id).setValue(user);
            updateUser();

            Toast.makeText(getContext(), "Account details edited successfully", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUser(final String id) {
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                updateUser();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Test", "onCancelled", databaseError.toException());
            }
        });
    }
}