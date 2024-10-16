package com.example.tingtingu_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class loginpage extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText referralCodeEditText;
    private String selectedLanguage;

    // Define the minimum length for the phone number
    private static final int MIN_PHONE_LENGTH = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                // Validate the phone number length
                if (phoneNumber.length() < MIN_PHONE_LENGTH) {
                    // Show an error message if the phone number is too short
                    Toast.makeText(loginpage.this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with the existing logic if the phone number is valid
                    Intent intentToDisplay = new Intent(loginpage.this, MainActivity.class);


                    // Start the 5th activity
                    startActivity(intentToDisplay);
                }
            }
        });
    }
}