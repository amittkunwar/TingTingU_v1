package com.example.tingtingu_v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class secondpage extends AppCompatActivity {

    private static final String PREFS_NAME = "user_session";  // Same as in loginpage
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        // If the user is logged in, skip this page and go to MainActivity
        if (isLoggedIn) {
            Intent intentToMain = new Intent(secondpage.this, MainActivity.class);
            startActivity(intentToMain);
            finish();  // Close secondpage activity
            return;  // Exit onCreate early
        }

        setContentView(R.layout.activity_secondpage);

        // Initialize the button
        Button button = findViewById(R.id.continue_button);

        // Set an OnClickListener to the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the loginpage
                Intent intent = new Intent(secondpage.this, loginpage.class);
                startActivity(intent);
            }
        });
    }
}
