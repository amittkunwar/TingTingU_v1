package com.example.tingtingu_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class firstpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        // Initialize the button by finding its ID
        Button button = findViewById(R.id.letstart); // Ensure the ID matches your layout

        // Set an OnClickListener to the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start MainActivity
                Intent intent = new Intent(firstpage.this, secondpage.class);

                // Pass data to MainActivity
                intent.putExtra("key", "Hello from firstpage");

                // Start MainActivity
                startActivity(intent);
            }
        });
    }
}
