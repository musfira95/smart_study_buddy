package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    Button uploadAudioBtn, logoutBtn, btnLastTranscription, btnRecordingList, btnProfile;
    Button btnExportPDF, btnSearch, btnTimetable; // timetable included
    SessionManager session;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        session = new SessionManager(this);

        // Initialize buttons
        uploadAudioBtn = findViewById(R.id.uploadAudioBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        btnLastTranscription = findViewById(R.id.btnLastTranscription);
        btnRecordingList = findViewById(R.id.btnRecordingList);
        btnProfile = findViewById(R.id.btnProfile);

        btnExportPDF = findViewById(R.id.btnExportPDF);
        btnSearch = findViewById(R.id.btnSearch);
        btnTimetable = findViewById(R.id.btnTimetable);

        dbHelper = new DatabaseHelper(this);

        // Upload Audio
        uploadAudioBtn.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, UploadAudioActivity.class));
        });

        // Recording List
        btnRecordingList.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, NotesActivity.class));
        });

        // Last transcription
        btnLastTranscription.setOnClickListener(v -> {
            String last = LastTranscriptionHolder.lastTranscription;
            if (last == null || last.trim().isEmpty()) {
                Toast.makeText(this, "No transcription available yet!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(DashboardActivity.this, NotesActivity.class);
            intent.putExtra("TRANSCRIPTION_TEXT", last);
            startActivity(intent);
        });

        // Profile
        btnProfile.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ProfileActivity.class)));

        // Export / PDF (dummy popup)
        btnExportPDF.setOnClickListener(v -> Toast.makeText(DashboardActivity.this, "Dummy PDF download popup", Toast.LENGTH_SHORT).show());

        // Search / Bookmark
        btnSearch.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, SearchActivity.class)));

        // Timetable
        btnTimetable.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, TimetableActivity.class));
        });

        // Logout
        logoutBtn.setOnClickListener(v -> {
            session.logoutUser();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
