package com.project.android.exampledictionary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.navigation_drawer.AllFeedbackFragment;
import com.project.android.exampledictionary.navigation_drawer.DictionaryFragment;
import com.project.android.exampledictionary.navigation_drawer.FavoriteFragment;
import com.project.android.exampledictionary.navigation_drawer.FeedbackFragment;
import com.project.android.exampledictionary.navigation_drawer.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DictionaryFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_dictionary);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dictionary:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new DictionaryFragment())
                        .commit();
                break;

            case R.id.nav_favorite:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FavoriteFragment())
                        .commit();
                break;

            case R.id.nav_all_feedback:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AllFeedbackFragment())
                        .commit();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();

                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy your dictionary every time with English Dictionary.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            case R.id.nav_feedback:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FeedbackFragment())
                        .commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Double back to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = true;
            }
        }, 2000);
    }
}
