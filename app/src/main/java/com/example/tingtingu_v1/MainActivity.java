package com.example.tingtingu_v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String PREFS_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private boolean doubleBackToExitPressedOnce = false;  // To track back press
    private Handler backPressHandler = new Handler(Looper.getMainLooper());  // To handle back press timing

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
            } else if (item.getItemId() == R.id.navMenu6) {
                // Logout functionality when navMenu6 is clicked
                logoutUser();
                return true;
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
        searchBar.setOnClickListener(v -> {
            // Log the action for debugging
            Log.d("MainActivity", "Search bar clicked. Navigating to Search activity");

            // Create an Intent to navigate to the new SearchActivity
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // Check if the drawer is open
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer if it's open
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            // Check if the current fragment is NOT HomeFragment
            if (!(currentFragment instanceof HomeFragment)) {
                // Load the HomeFragment and set the bottom navigation to Home
                loadFragment(new HomeFragment(), false);
                bottomNavigationView.setSelectedItemId(R.id.navHome);
            } else {
                // If user presses back once, show a toast
                if (doubleBackToExitPressedOnce) {
                    // If back is pressed twice within 2 seconds, show exit confirmation dialog
                    new AlertDialog.Builder(this)
                            .setMessage("Do you want to exit the app?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> finishAffinity())
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    // First back press: Show a toast message
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                    // Reset the double back press flag after 2 seconds
                    backPressHandler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                }
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

    // Method to log out the user
    private void logoutUser() {
        // Clear the session data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Clear all saved data
        editor.apply();

        // Redirect the user to the firstpage
        Intent intent = new Intent(MainActivity.this, firstpage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
        startActivity(intent);
        finish();  // Close the MainActivity
    }
}
