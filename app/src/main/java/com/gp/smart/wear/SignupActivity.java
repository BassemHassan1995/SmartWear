package com.gp.smart.wear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gp.smart.wear.Entities.Cart;
import com.gp.smart.wear.Entities.User;

public class SignupActivity extends AppCompatActivity {

    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mMobileNumberField;
    private Button mSignupButton;
    private TextView mLoginLink;

    private String user_email = "";

    private static final String TAG = "Signup";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Views
        mFirstNameField = (EditText) findViewById(R.id.input_first_name);
        mLastNameField = (EditText) findViewById(R.id.input_last_name);
        mEmailField = (EditText) findViewById(R.id.input_email);
        mPasswordField = (EditText) findViewById(R.id.input_password);
        mMobileNumberField = (EditText) findViewById(R.id.input_mobile);
        mSignupButton = (Button) findViewById(R.id.btn_signup);
        mLoginLink = (TextView) findViewById(R.id.link_login);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                createAccount(email, password);
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user_email = user.getEmail();
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

//        ProgressDialog.show(this, "Login" ,"Please wait while Signing up" , true);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, R.string.signup_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String first_name = String.valueOf(mFirstNameField.getText());
                            String last_name = String.valueOf(mLastNameField.getText());
                            String email = String.valueOf(mEmailField.getText());
                            String mobile_number = String.valueOf(mMobileNumberField.getText());
                            Cart cart = new Cart();
                            FirebaseUser firebase_user = task.getResult().getUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(first_name + " " + last_name)
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
                            String id = firebase_user.getUid();
                            User user = new User(first_name,last_name,email,mobile_number,cart);

                            //Add the new user to the Firebase Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference();
                            final DatabaseReference usersRef = ref.child("users");
                            usersRef.child(id).setValue(user);

                            //Add the created User ID to the shared preferences
                            SharedPreferences.Editor preferences = getSharedPreferences("UserFile" , MODE_PRIVATE).edit();
                            preferences.putString("user_id",id);
                            preferences.apply();

                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            intent.putExtra("user_name" , firebase_user.getDisplayName());
                            intent.putExtra("user_email" , firebase_user.getEmail());

                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else if (!email.contains("@")){
            mEmailField.setError("Invalid form");
        }
        else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        String confirm_password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else if (mPasswordField.length()<8){
            mPasswordField.setError("Password should be more than 8 characters");
            valid = false;
        } else{
            mPasswordField.setError(null);
        }

        if (!password.equals(confirm_password)){
            mPasswordField.setError("Passwords don't match");
        }
        return valid;
    }
}