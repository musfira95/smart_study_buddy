package com.example.smartstudybuddy2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private ArrayList<NoteItem> noteList;
    private DatabaseHelper dbHelper;

    private EditText etTitle, etContent;
    private Button btnSave, btnSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView = findViewById(R.id.notesRecyclerView);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSaveNote);
        btnSummary = findViewById(R.id.btnSummary);

        dbHelper = new DatabaseHelper(this);
        noteList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(this, noteList);
        recyclerView.setAdapter(adapter);

        // ------------------- Receive transcription from RecordMicActivity -------------------
        String transcription = getIntent().getStringExtra("TRANSCRIPTION_TEXT");
        if (transcription != null && !transcription.isEmpty()) {
            etContent.setText(transcription); // show fresh transcription after stop recording
        }

        // ------------------- Load existing notes -------------------
        loadNotes(); // RecyclerView only, EditText untouched

        // ------------------- Save button -------------------
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Enter title and content", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertNote(title, content);
            if (inserted) {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
                loadNotes();
                etTitle.setText("");
                etContent.setText("");
            } else {
                Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
            }
        });

        // ------------------- Summary button -------------------
        btnSummary.setOnClickListener(v -> {
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "No transcription to summarize", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(NotesActivity.this, SummaryActivity.class);
            intent.putExtra("transcriptionText", content);
            startActivity(intent);
        });
    }

    // ------------------- Load notes -------------------
    private void loadNotes() {
        noteList.clear();
        Cursor cursor = dbHelper.getAllNotes();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                noteList.add(new NoteItem(id, title, content));
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }
}
