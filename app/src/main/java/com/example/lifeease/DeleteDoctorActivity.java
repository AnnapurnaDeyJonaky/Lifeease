package com.example.lifeease;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteDoctorActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView textViewSpecialization;
    private TextView textViewFee;
    private ImageView imageViewDoctor;
    private Button buttonDelete;
    private Button buttonSearch;
    private TextView textViewDoctorId;

    private DatabaseHelper databaseHelper;
    private byte[] doctorImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_doctor);

        editTextName = findViewById(R.id.text_view_doctor_name_delete);
        textViewSpecialization = findViewById(R.id.text_view_doctor_specialization_delete);
        textViewFee = findViewById(R.id.text_view_doctor_fee_delete);
        textViewDoctorId = findViewById(R.id.text_view_doctor_id);
        imageViewDoctor = findViewById(R.id.image_view_doctor_delete);
        buttonDelete = findViewById(R.id.button_delete_delete);
        buttonSearch = findViewById(R.id.button_search_delete);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchDoctor());
        buttonDelete.setOnClickListener(view -> deleteDoctor());

        Button btnBackDelete = findViewById(R.id.button_back_delete);
        btnBackDelete.setOnClickListener(v -> {
            Toast.makeText(DeleteDoctorActivity.this, "Navigating back", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteDoctorActivity.this, AdminHomeActivity.class);
            startActivity(intent);
        });
    }

    private void searchDoctor() {
        String doctorName = editTextName.getText().toString().trim();
        if (doctorName.isEmpty()) {
            Toast.makeText(this, "Please enter a doctor name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getDoctorByName(doctorName);
        if (cursor != null && cursor.moveToFirst()) {
            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_ID));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
            double fee = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FEE));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_IMAGE_URI));

            textViewSpecialization.setText(specialization);
            textViewFee.setText(String.valueOf(fee));
            textViewDoctorId.setText("Doctor ID: " + doctorId);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewDoctor.setImageBitmap(bitmap);
                doctorImageByteArray = image;
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Doctor not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDoctor() {
        String doctorName = editTextName.getText().toString().trim();
        if (doctorName.isEmpty()) {
            Toast.makeText(this, "Please enter a doctor name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDeleted = databaseHelper.deleteDoctor(doctorName);

        if (isDeleted) {
            Toast.makeText(this, "Doctor deleted successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to delete doctor", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        textViewSpecialization.setText("");
        textViewFee.setText("");
        textViewDoctorId.setText("");
        imageViewDoctor.setImageResource(0); // Clear image view
    }
}
