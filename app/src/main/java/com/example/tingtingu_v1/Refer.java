package com.example.tingtingu_v1;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Refer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_refer);

        // Apply window insets for system bars (Edge-to-Edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Finding views by their IDs
        TextView referralCodeText = findViewById(R.id.referral_code_text);
        TextView tapToCopyButton = findViewById(R.id.tap_to_copy_button);
        Button referNowButton = findViewById(R.id.refer_now_button);
        ImageView backButton = findViewById(R.id.back_1); // Back button

        // Setting referral code (for testing, you can get this from a source like a database)
        referralCodeText.setText("AK6969"); // Example referral code

        // "Tap to Copy" button functionality
        tapToCopyButton.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Referral Code", referralCodeText.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(Refer.this, "Referral Code Copied!", Toast.LENGTH_SHORT).show();
        });

        // "Refer Now" button functionality
        referNowButton.setOnClickListener(view -> {
            String referralCode = referralCodeText.getText().toString();
            String referralLink = "https://play.google.com/store/apps/details?id=com.kabaladigitalf.tingtingu&hl=en_IN" + referralCode;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Join our app with my referral code: " + referralCode +
                    " Click here: " + referralLink);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // "Back" button functionality to navigate to MainActivity
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Refer.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}
