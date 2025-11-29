package com.example.smartstudybuddy2;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordingListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Recording> recordingList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_list);

        listView = findViewById(R.id.recordingListView);
        dbHelper = new DatabaseHelper(this);

        loadRecordings();
    }

    private void loadRecordings() {
        try {
            recordingList = dbHelper.getAllRecordings();
            if(recordingList == null) recordingList = new ArrayList<>();

            if (recordingList.isEmpty()) {
                Toast.makeText(this, "No recordings found!", Toast.LENGTH_SHORT).show();
            }

            RecordingListAdapter adapter = new RecordingListAdapter(this, recordingList, new RecordingListAdapter.OnRecordingClickListener() {
                @Override
                public void onPlayClicked(Recording recording) {
                    Toast.makeText(RecordingListActivity.this, "Cannot play this recording.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDeleteClicked(Recording recording) {
                    dbHelper.deleteRecording(recording.getId());
                    loadRecordings();
                    Toast.makeText(RecordingListActivity.this, "Recording deleted", Toast.LENGTH_SHORT).show();
                }
            });

            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading recordings: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
