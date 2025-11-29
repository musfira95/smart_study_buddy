package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadAudioActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 1;
    private static final int RECORD_AUDIO_REQUEST = 200;

    private Button uploadButton, processButton, recordButton;
    private TextView fileNameText;
    private Uri audioUri;
    private String fileName;
    private long fileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        uploadButton = findViewById(R.id.uploadButton);
        processButton = findViewById(R.id.processButton);
        recordButton = findViewById(R.id.recordButton);
        fileNameText = findViewById(R.id.fileNameText);

        processButton.setEnabled(false);

        // ---------- Upload MP3 ----------
        uploadButton.setOnClickListener(v -> openAudioPicker());

        // ---------- Process Audio ----------
        processButton.setOnClickListener(v -> {
            if (audioUri != null) {
                Intent intent = new Intent(UploadAudioActivity.this, ProcessAudioActivity.class);
                intent.putExtra("fileName", fileName);
                intent.putExtra("fileSize", fileSize);
                intent.putExtra("audioUri", audioUri.toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select an MP3 first", Toast.LENGTH_SHORT).show();
            }
        });

        // ---------- Record Audio ----------
        recordButton.setOnClickListener(v -> {
            Intent intent = new Intent(UploadAudioActivity.this, RecordMicActivity.class);
            startActivityForResult(intent, RECORD_AUDIO_REQUEST);
        });
    }

    private void openAudioPicker() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select MP3"), PICK_AUDIO_REQUEST);
    }

    // ---------- Handle Results ----------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RecordMicActivity ka result
        if (requestCode == RECORD_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            String recordedPath = data.getStringExtra("RECORDED_FILE_PATH");
            if (recordedPath != null) {
                // Sirf Toast dikhaye, TextView ko update na kare
                Toast.makeText(this, "Recording received", Toast.LENGTH_SHORT).show();
            }

        }


        // MP3 Picker ka result
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            audioUri = data.getData();
            if (audioUri != null) {
                fileName = getFileName(audioUri);

                // ✅ Check if file is MP3
                if (!fileName.toLowerCase().endsWith(".mp3")) {
                    Toast.makeText(this, "Please select an MP3 audio file only", Toast.LENGTH_SHORT).show();
                    audioUri = null;
                    fileName = null;
                    fileSize = 0;
                    fileNameText.setText("No file selected");
                    processButton.setEnabled(false);
                    return;
                }

                fileSize = getFileSize(audioUri);
                fileNameText.setText("Selected: " + fileName + " (" + fileSize + " KB)");
                processButton.setEnabled(true);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = "";
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result.isEmpty()) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private long getFileSize(Uri uri) {
        long size = 0;
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                size = cursor.getLong(sizeIndex);
            }
        }
        return size / 1024; // KB
    }
}
