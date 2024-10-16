package com.example.tingtingu_v1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EdgeToEdge.enable(this);

        Intent iHome = new Intent(Splash.this, firstpage.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(iHome);
                finish();
                // Optional: Call finish() to close the splash screen
            }
        }, 2000); // delayMillis is set here to 4000 milliseconds (4 seconds)




    }
}