package com.example.tingtingu_v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class loginpage extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText referralCodeEditText;
    private String selectedLanguage;
    private FirebaseFirestore db;

    // Constants for SharedPreferences
    private static final String PREFS_NAME = "user_session";  // SharedPreferences file name
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";  // Key to store login status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        // If the user is logged in, directly open the MainActivity
        if (isLoggedIn) {
            Intent intentToDisplay = new Intent(loginpage.this, MainActivity.class);
            startActivity(intentToDisplay);
            finish();  // Close login activity
            return;  // Exit onCreate early
        }

        setContentView(R.layout.activity_loginpage);

        // Get the selected language from the previous activity
        Intent intent = getIntent();
        selectedLanguage = intent.getStringExtra("SELECTED_LANGUAGE");

        // Initialize EditText fields
        phoneNumberEditText = findViewById(R.id.number);
        referralCodeEditText = findViewById(R.id.refer);

        // Initialize the Agree and Continue button
        Button button = findViewById(R.id.agree_continue_button);

        // Set an OnClickListener to the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input from the EditTexts
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String referralCode = referralCodeEditText.getText().toString().trim();

                if (phoneNumber.isEmpty()) {
                    // Show an error message if the phone number is empty
                    Toast.makeText(loginpage.this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save login status in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);  // Set login status to true
                editor.apply();  // Save changes asynchronously

                // Save user data to Firestore
                saveUserData(phoneNumber, referralCode);

                // Proceed to the main activity
                Intent intentToDisplay = new Intent(loginpage.this, MainActivity.class);
                startActivity(intentToDisplay);
                finish();  // Close login activity
            }
        });
    }

    // Method to save user data to Firestore
    private void saveUserData(String phoneNumber, String referralCode) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("phoneNumber", phoneNumber);
        userData.put("referralCode", referralCode);

        db.collection("users").add(userData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show());
    }
}
