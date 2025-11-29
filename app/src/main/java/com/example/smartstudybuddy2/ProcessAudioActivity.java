package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ProcessAudioActivity extends AppCompatActivity {

    TextView transcriptionText;
    Button processAudioBtn, viewDetailsBtn, summarizeBtn;
    String fileName, audioUri;
    long fileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_audio);

        transcriptionText = findViewById(R.id.resultTextView);
        processAudioBtn = findViewById(R.id.processButton);
        viewDetailsBtn = findViewById(R.id.viewDetailsBtn);
        summarizeBtn = findViewById(R.id.summarizeButton); // naya button

        // Receive data from UploadAudioActivity
        fileName = getIntent().getStringExtra("fileName");
        fileSize = getIntent().getLongExtra("fileSize", 0);
        audioUri = getIntent().getStringExtra("audioUri");

        // ---------------------------
        // Process Audio Button
        // ---------------------------
        processAudioBtn.setOnClickListener(v -> {
            // Simulated transcription
            String transcription = "Transcript for: " + fileName +
                    "\n\nArtificial Intelligence helps automate study and note-taking tasks.";

            // Show transcription only in this screen
            transcriptionText.setText(transcription);

            // Save latest transcription for dashboard if needed
            LastTranscriptionHolder.lastTranscription = transcription;

            // Show Summarize button now
            summarizeBtn.setVisibility(Button.VISIBLE);
        });

        // ---------------------------
        // Summarize Button
        // ---------------------------
        summarizeBtn.setOnClickListener(v -> {
            String transcription = transcriptionText.getText().toString();
            if (!transcription.isEmpty()) {
                Intent summaryIntent = new Intent(ProcessAudioActivity.this, SummaryActivity.class);
                summaryIntent.putExtra("transcriptionText", transcription);
                startActivity(summaryIntent);
            }
        });

        // ---------------------------
        // View Details Button
        // ---------------------------
        viewDetailsBtn.setOnClickListener(v -> {
            Intent detailsIntent = new Intent(ProcessAudioActivity.this, ViewUploadedDetailsActivity.class);
            detailsIntent.putExtra("fileName", fileName);
            detailsIntent.putExtra("fileSize", fileSize);
            detailsIntent.putExtra("uploadTime", new java.util.Date().toString());
            startActivity(detailsIntent);
        });
    }
}
