package com.example.smartstudybuddy2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TranscriptionActivity extends AppCompatActivity {

    private TextView tvTranscriptionContent;
    private Button btnSummarize;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcription);

        tvTranscriptionContent = findViewById(R.id.tvTranscriptionContent);
        btnSummarize = findViewById(R.id.btnSummarize);
        db = new DatabaseHelper(this);

        // Get transcription from previous activity (record or upload)
        String transcriptionText = getIntent().getStringExtra("transcriptionText");
        if (transcriptionText != null) {
            tvTranscriptionContent.setText(transcriptionText);

            // Save transcription as a note
            boolean saved = db.insertNote("Auto Note", transcriptionText);
            if (saved) {
                Toast.makeText(this, "Transcription saved as note!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
            }
        }

        // Go to summary
        btnSummarize.setOnClickListener(v -> {
            Intent intent = new Intent(TranscriptionActivity.this, SummaryActivity.class);
            intent.putExtra("transcriptionText", transcriptionText);
            startActivity(intent);
        });
    }
}
