package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Bind views
        TextView textViewMessage = findViewById(R.id.text_view_message);
        Button btnDoctor=findViewById(R.id.btn_doctors);

        btnDoctor.setOnClickListener(v -> {

                Intent intent = new Intent(ConfirmationActivity.this, FindDoctorActivity.class);

                startActivity(intent);
                finish();
        });
        // Retrieve data from Intent
        String patientName = getIntent().getStringExtra("patientName");
        String doctorName = getIntent().getStringExtra("doctorName");
        String appointmentDate = getIntent().getStringExtra("appointmentDate");
        String visitingTime = getIntent().getStringExtra("visitingTime");

        // Set confirmation message
        String confirmationMessage = "Appointment booked successfully!\n\n" +
                "Patient: " + patientName + "\n" +
                "Doctor: " + doctorName + "\n" +
                "Date: " + appointmentDate + "\n" +
                "Time: " + visitingTime;
        textViewMessage.setText(confirmationMessage);
    }
}
