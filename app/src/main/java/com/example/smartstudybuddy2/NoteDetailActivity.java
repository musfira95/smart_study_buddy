package com.example.smartstudybuddy2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnUpdate, btnDelete;
    private DatabaseHelper dbHelper;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        etTitle = findViewById(R.id.etDetailTitle);
        etContent = findViewById(R.id.etDetailContent);
        btnUpdate = findViewById(R.id.btnUpdateNote);
        btnDelete = findViewById(R.id.btnDeleteNote);

        dbHelper = new DatabaseHelper(this);

        // Get data from intent
        noteId = getIntent().getIntExtra("noteId", -1);
        String title = getIntent().getStringExtra("noteTitle");
        String content = getIntent().getStringExtra("noteContent");

        etTitle.setText(title);
        etContent.setText(content);

        // Update note
        btnUpdate.setOnClickListener(v -> {
            String updatedTitle = etTitle.getText().toString().trim();
            String updatedContent = etContent.getText().toString().trim();

            if(updatedTitle.isEmpty() || updatedContent.isEmpty()){
                Toast.makeText(NoteDetailActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updateNote(noteId, updatedTitle, updatedContent);
            if(updated){
                Toast.makeText(NoteDetailActivity.this, "Note updated successfully!", Toast.LENGTH_SHORT).show();
                finish(); // go back to NotesActivity
            } else {
                Toast.makeText(NoteDetailActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete note
        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteNote(noteId);
                        Toast.makeText(NoteDetailActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                        finish(); // go back to NotesActivity
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
