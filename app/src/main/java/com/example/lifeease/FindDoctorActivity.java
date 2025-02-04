package com.example.lifeease;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class FindDoctorActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lstDoctors;
    private Button btnViewAppointments;
    private SearchView searchViewDoctors;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        dbHelper = new DatabaseHelper(this);
        lstDoctors = findViewById(R.id.findDoctorsListView);
        btnViewAppointments = findViewById(R.id.btnViewAppointments);
        searchViewDoctors = findViewById(R.id.doctorSearchView);


        btnViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(FindDoctorActivity.this, ViewAppointmentsActivity.class);
            startActivity(intent);
        });

        displayDoctors();

//        lstDoctors.setOnItemClickListener((parent, view, position, id) -> {
//            try {
//                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                if (cursor != null && cursor.moveToPosition(position)) {
//                    String doctorName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DOCTOR_NAME));
//                    String specialization = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_SPECIALIZATION));
//
//                    Intent intent = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
//                    intent.putExtra("doctorName", doctorName);
//                    intent.putExtra("specialization", specialization);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, "Unable to retrieve doctor details.", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                Log.e("FindDoctorActivity", "Error retrieving doctor details", e);
//                Toast.makeText(this, "Error retrieving doctor details.", Toast.LENGTH_SHORT).show();
//            }
//        });

        lstDoctors.setOnItemClickListener((parent, view, position, id) -> {
            try {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null && cursor.moveToPosition(position)) {
                    String doctorName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DOCTOR_NAME));
                    String specialization = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_SPECIALIZATION));
                    String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PHONE));
                    double fee = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_FEE));
                    String visitingTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_VISITING_TIME));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COL_DOCTOR_IMAGE_URI));

                    Intent intent = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
                    intent.putExtra("doctorName", doctorName);
                    intent.putExtra("specialization", specialization);
                    intent.putExtra("phone", phone);
                    intent.putExtra("fee", fee);
                    intent.putExtra("visitingTime", visitingTime);
                    intent.putExtra("image", image);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Unable to retrieve doctor details.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("FindDoctorActivity", "Error retrieving doctor details", e);
                Toast.makeText(this, "Error retrieving doctor details.", Toast.LENGTH_SHORT).show();
            }
        });


        searchViewDoctors.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query); // Call handleSearch on submit
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDoctors(newText); // Update search results dynamically without closing the keyboard
                return true;
            }
        });

    }

    private void handleSearch(String query) {
        searchDoctors(query);
    }

    private void displayDoctors() {
        try {
            Cursor cursor = dbHelper.getAllDoctors();
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(this, "No doctors found", Toast.LENGTH_SHORT).show();
                return;
            }
            if (adapter == null) {
                adapter = new DoctorAdapter(this, cursor, 0);
                lstDoctors.setAdapter(adapter);
            } else {
                adapter.changeCursor(cursor);
            }
        } catch (Exception e) {
            Log.e("FindDoctorActivity", "Error displaying doctors", e);
            Toast.makeText(this, "Error loading doctors.", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchDoctors(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                displayDoctors();
                return;
            }

            // Perform case-insensitive search in the database
            Cursor cursor = dbHelper.getSearchDoctorByNameOrSpecialization(query);
            if (cursor != null && cursor.getCount() > 0) {
                if (adapter == null) {
                    adapter = new DoctorAdapter(this, cursor, 0);
                    lstDoctors.setAdapter(adapter);
                } else {
                    adapter.changeCursor(cursor);
                }
            } else {
                Toast.makeText(this, "No doctors found matching the query.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("FindDoctorActivity", "Error during search", e);
            Toast.makeText(this, "Error searching for doctors.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }
}
