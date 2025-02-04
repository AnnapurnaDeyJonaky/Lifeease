package com.example.lifeease;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAppointmentsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewBookings;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Reference ListView
        listViewBookings = findViewById(R.id.list_view_bookings);

        // Load and display bookings
        loadBookings();
    }

    private void loadBookings() {
        Cursor cursor = dbHelper.getAllBookings();
        if (cursor != null && cursor.getCount() > 0) {
            adapter = new BookingAdapter(this, cursor, 0);
            listViewBookings.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No bookings found.", Toast.LENGTH_SHORT).show();
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
