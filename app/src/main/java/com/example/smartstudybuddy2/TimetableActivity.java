package com.example.smartstudybuddy2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TimetableActivity extends AppCompatActivity {

    Button btnAddReminder, btnViewCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        btnAddReminder = findViewById(R.id.btnAddReminder);
        btnViewCalendar = findViewById(R.id.btnViewCalendar);

        btnAddReminder.setOnClickListener(v ->
                Toast.makeText(this, "Reminder added (dummy)", Toast.LENGTH_SHORT).show()
        );

        btnViewCalendar.setOnClickListener(v ->
                Toast.makeText(this, "Calendar view (dummy)", Toast.LENGTH_SHORT).show()
        );
    }
}
