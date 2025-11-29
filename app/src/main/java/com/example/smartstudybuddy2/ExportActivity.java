package com.example.smartstudybuddy2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ExportActivity extends AppCompatActivity {

    Button btnExportPDF, btnExportTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        btnExportPDF = findViewById(R.id.btnExportPDF);
        btnExportTXT = findViewById(R.id.btnExportTXT);

        btnExportPDF.setOnClickListener(v ->
                Toast.makeText(this, "PDF exported (dummy)", Toast.LENGTH_SHORT).show()
        );

        btnExportTXT.setOnClickListener(v ->
                Toast.makeText(this, "Text file exported (dummy)", Toast.LENGTH_SHORT).show()
        );
    }
}
