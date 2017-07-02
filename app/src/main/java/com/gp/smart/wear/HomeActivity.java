package com.gp.smart.wear;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gp.smart.wear.NavigationDrawer_Fragments.AboutFragment;
import com.gp.smart.wear.NavigationDrawer_Fragments.AccountFragment;
import com.gp.smart.wear.NavigationDrawer_Fragments.BrandsFragment;
import com.gp.smart.wear.NavigationDrawer_Fragments.OrdersFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.search_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = BrandsFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();

        FirebaseUser firebase_user= FirebaseAuth.getInstance().getCurrentUser();
        String user_name = firebase_user.getDisplayName();
        Toast.makeText(this, user_name, Toast.LENGTH_SHORT).show();
        String user_email = firebase_user.getEmail();
//        String user_name = intent.getStringExtra("user_name");
//        String user_email = intent.getStringExtra("user_email");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        LinearLayout header = (LinearLayout) navigationView.getHeaderView(0);
        TextView user_name_textview = (TextView) header.findViewById(R.id.user_name);
        if (user_name != null) {
            user_name_textview.setText(user_name);
        }
        TextView user_email_textview = (TextView) header.findViewById(R.id.user_mail);
        user_email_textview.setText(user_email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = BrandsFragment.class;

        if (id == R.id.nav_watches) {
            fragmentClass = BrandsFragment.class;
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
        } else if (id == R.id.nav_account) {
            fragmentClass = AccountFragment.class;
        } else if (id == R.id.nav_contact_mail) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "bassem.hassan1995@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        } else if (id == R.id.nav_contact_phone) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + "01063007508"));
            if (dialIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(dialIntent);
            }

        } else if (id == R.id.nav_my_orders) {
            fragmentClass = OrdersFragment.class;

        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_exit) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit)
                    .setMessage(R.string.exit_confirmation)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the app
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.ic_info_black_48dp)
                    .show();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}