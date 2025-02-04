package com.example.lifeease;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class BookingAdapter extends CursorAdapter {

    public BookingAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewPatientName = view.findViewById(R.id.text_view_patient_name);
        TextView textViewDoctorName = view.findViewById(R.id.text_view_doctor_name);
        TextView textViewDate = view.findViewById(R.id.text_view_date);
        TextView textViewTime = view.findViewById(R.id.text_view_time);

        String patientName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PATIENT_NAME));
        String doctorName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_BOOKED));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIME));

        textViewPatientName.setText("Patient: " + patientName);
        textViewDoctorName.setText("Doctor: " + doctorName);
        textViewDate.setText("Date: " + date);
        textViewTime.setText("Time: " + time);
    }
}
