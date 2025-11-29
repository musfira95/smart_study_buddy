package com.example.smartstudybuddy2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private Button btnAbout, btnHelp, btnLogout;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnAbout = findViewById(R.id.btnAbout);
        btnHelp = findViewById(R.id.btnHelp);
        btnLogout = findViewById(R.id.btnLogout);

        // Set user info
        tvName.setText(session.getUserName());
        tvEmail.setText(session.getUserEmail());

//        // About button
//        btnAbout.setOnClickListener(v -> {
//            Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
//            startActivity(intent);
//        });

//        // Help button
//        btnHelp.setOnClickListener(v -> {
//            Intent intent = new Intent(ProfileActivity.this, HelpActivity.class);
//            startActivity(intent);
//        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
