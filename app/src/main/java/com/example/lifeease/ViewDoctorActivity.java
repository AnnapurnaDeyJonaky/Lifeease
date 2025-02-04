package com.example.lifeease;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewDoctorActivity extends AppCompatActivity {
    private ListView listViewDoctors;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        listViewDoctors = findViewById(R.id.list_view_products);
        databaseHelper = new DatabaseHelper(this);

        displayDoctors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the displayed doctors
        displayDoctors();
    }

    private void displayDoctors() {
        Cursor cursor = databaseHelper.getAllDoctors();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No doctors found", Toast.LENGTH_SHORT).show();
            return;
        }
        DoctorAdapter adapter = new DoctorAdapter(this, cursor, 0);
        listViewDoctors.setAdapter(adapter);
    }
}
