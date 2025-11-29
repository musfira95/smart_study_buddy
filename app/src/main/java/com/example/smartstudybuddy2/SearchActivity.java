package com.example.smartstudybuddy2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    String[] dummyNotes = {
            "Physics Chapter 1 Notes",
            "Math Lecture 2 Summary",
            "Chemistry Quiz 1",
            "History Notes",
            "Biology Flashcards"
    };
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dummyNotes);
        listView.setAdapter(adapter);

        // Filter notes on search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
