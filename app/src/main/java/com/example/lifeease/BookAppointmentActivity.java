package com.example.lifeease;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Bind views
        TextView textViewName = findViewById(R.id.text_view_doctor_name);
        ImageView imageViewDoctor = findViewById(R.id.image_view_doctor);
        EditText editTextPatientName = findViewById(R.id.edit_text_patient_name);
        EditText editTextDate = findViewById(R.id.edit_text_appointment_date);
        Button btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);

        // Retrieve data from Intent
        String doctorName = getIntent().getStringExtra("doctorName");
        byte[] image = getIntent().getByteArrayExtra("image");

        // Set data to views
        textViewName.setText(doctorName);
        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageViewDoctor.setImageBitmap(bitmap);
        } else {
            imageViewDoctor.setImageResource(R.drawable.person);
        }

//        btnConfirmAppointment.setOnClickListener(v -> {
//            String patientName = editTextPatientName.getText().toString().trim();
//            String appointmentDate = editTextDate.getText().toString().trim();
//
//            String visitingTime = getIntent().getStringExtra("visitingTime"); // Passed from DoctorDetailsActivity
//
//            if (patientName.isEmpty() || appointmentDate.isEmpty()) {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Database helper instance
//            DatabaseHelper dbHelper = new DatabaseHelper(this);
//
//            // Insert booking into the database
//            boolean isInserted = dbHelper.insertBooking(patientName, doctorName, appointmentDate, visitingTime);
//
//            if (isInserted) {
//                Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
//                finish(); // Close the activity
//            } else {
//                Toast.makeText(this, "Failed to book appointment. Try again.", Toast.LENGTH_SHORT).show();
//            }
//        });



        btnConfirmAppointment.setOnClickListener(v -> {
            String patientName = editTextPatientName.getText().toString().trim();
            String appointmentDate = editTextDate.getText().toString().trim();
            String visitingTime = getIntent().getStringExtra("visitingTime"); // Passed from DoctorDetailsActivity

            if (patientName.isEmpty() || appointmentDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Database helper instance
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            // Insert booking into the database
            boolean isInserted = dbHelper.insertBooking(patientName, doctorName, appointmentDate, visitingTime);

            if (isInserted) {
                // Navigate to ConfirmationActivity
                Intent intent = new Intent(BookAppointmentActivity.this, ConfirmationActivity.class);
                intent.putExtra("patientName", patientName);
                intent.putExtra("doctorName", doctorName);
                intent.putExtra("appointmentDate", appointmentDate);
                intent.putExtra("visitingTime", visitingTime);
                startActivity(intent);
                finish(); // Close the current activity
            } else {
                Toast.makeText(this, "Failed to book appointment. Try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
