package com.example.tingtingu_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;  // Added for logging
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Set the default fragment (HomeFragment in this case)
        loadFragment(new HomeFragment(), true);

        // Bottom navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navHome) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navChat) {
                selectedFragment = new ChatFragment();
            } else if (item.getItemId() == R.id.navTafreeh) {
                selectedFragment = new TafreehFragment();
            } else if (item.getItemId() == R.id.navProfile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.navSettings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, false);
                return true;
            }

            return false;
        });

        // Navigation drawer item selection
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navMenu) {
                selectedFragment = new HomeFragment();
                bottomNavigationView.setSelectedItemId(R.id.navHome);
            } else if (item.getItemId() == R.id.navMenu2) {
                selectedFragment = new SettingsFragment();
                bottomNavigationView.setSelectedItemId(R.id.navSettings);
            } else if (item.getItemId() == R.id.navMenu3) {
                selectedFragment = new ChatFragment();
                bottomNavigationView.setSelectedItemId(R.id.navChat);
            } else if (item.getItemId() == R.id.navMenu4) {
                selectedFragment = new TafreehFragment();
                bottomNavigationView.setSelectedItemId(R.id.navTafreeh);
            } else if (item.getItemId() == R.id.navMenu5) {
                selectedFragment = new ProfileFragment();
                bottomNavigationView.setSelectedItemId(R.id.navProfile);
            }

            // Load the selected fragment if it's not null
            if (selectedFragment != null) {
                loadFragment(selectedFragment, false);
                // Close the drawer after item selection
                drawerLayout.closeDrawers();
                return true;
            }

            return false;
        });

        // Locate the TextView acting as the search bar
        TextView searchBar = findViewById(R.id.search_bar);

        // Set the onClick listener to the TextView (acting as the search bar)
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log the action for debugging
                Log.d("MainActivity", "Search bar clicked. Navigating to Search activity");

                // Create an Intent to navigate to the new SearchActivity
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Check if the drawer is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer if it's open
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Check if the current fragment is NOT HomeFragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (!(currentFragment instanceof HomeFragment)) {
                // Load the HomeFragment and set the bottom navigation to Home
                loadFragment(new HomeFragment(), false);
                bottomNavigationView.setSelectedItemId(R.id.navHome);
            } else {
                // Show exit confirmation dialog
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to exit the app?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> finishAffinity())
                        .setNegativeButton("No", null)
                        .show();
            }
        }
    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START); // Opens the drawer from the left
    }

    // Method to load fragments
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        fragmentTransaction.commit();
    }
}