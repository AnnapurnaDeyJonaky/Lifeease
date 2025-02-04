package com.example.lifeease;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        // Bind views
        TextView textViewName = findViewById(R.id.text_view_doctor_name);
        TextView textViewSpecialization = findViewById(R.id.text_view_specialization);
        TextView textViewPhone = findViewById(R.id.text_view_phone);
        TextView textViewFee = findViewById(R.id.text_view_fee);
        TextView textViewVisitingTime = findViewById(R.id.text_view_visiting_time);
        ImageView imageViewDoctor = findViewById(R.id.image_view_doctor);
        Button btnBookAppointment = findViewById(R.id.btn_book_appointment);

        // Retrieve data from the Intent
        String doctorName = getIntent().getStringExtra("doctorName");
        String specialization = getIntent().getStringExtra("specialization");
        String phone = getIntent().getStringExtra("phone");
        double fee = getIntent().getDoubleExtra("fee", 0.0);
        String visitingTime = getIntent().getStringExtra("visitingTime");
        byte[] image = getIntent().getByteArrayExtra("image");

        // Set data to views
        textViewName.setText(doctorName);
        textViewSpecialization.setText("Specialization: " + specialization);
        textViewPhone.setText("Phone: " + phone);
        textViewFee.setText("Fee: ₹" + fee);
        textViewVisitingTime.setText("Visiting Time: " + visitingTime);

        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageViewDoctor.setImageBitmap(bitmap);
        } else {
            imageViewDoctor.setImageResource(R.drawable.person); // Default placeholder
        }

        // Handle Book Appointment button click
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
            intent.putExtra("doctorName", doctorName);
            intent.putExtra("specialization", specialization);
            intent.putExtra("phone", phone);
            intent.putExtra("fee", fee);
            intent.putExtra("visitingTime", visitingTime);
            intent.putExtra("image", image);
            startActivity(intent);
        });
    }
}
