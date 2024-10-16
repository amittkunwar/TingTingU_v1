package com.example.tingtingu_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class secondpage extends AppCompatActivity {

    private String selectedLanguage; // Variable to hold selected language

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);


        Button button = findViewById(R.id.continue_button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(secondpage.this, loginpage.class);




                // Start LoginActivity
                startActivity(intent);
            }
        });
    }
}
