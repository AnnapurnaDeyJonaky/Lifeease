package com.example.lifeease;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateDoctorActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName, editTextSpecialization, editTextPhone, editTextFee, editTextVisitingTime;
    private ImageView imageViewDoctor;
    private Button buttonUpdate, buttonSelectImage, buttonSearch;
    private TextView textViewDoctorId;
    private byte[] doctorImageByteArray;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor);

        // Initialize Views
        editTextName = findViewById(R.id.edit_text_doctor_name_update);
        editTextSpecialization = findViewById(R.id.edit_text_specialization_update);
        editTextPhone = findViewById(R.id.edit_text_phone_update);
        editTextFee = findViewById(R.id.edit_text_fee_update);
        editTextVisitingTime = findViewById(R.id.edit_text_visiting_time_update);
        imageViewDoctor = findViewById(R.id.image_view_doctor_update);
        buttonUpdate = findViewById(R.id.button_update_in_update);
        buttonSelectImage = findViewById(R.id.button_select_image_update);
        buttonSearch = findViewById(R.id.button_search);
        textViewDoctorId = findViewById(R.id.text_view_doctor_id_update);

        // Database helper
        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchDoctor());
        buttonSelectImage.setOnClickListener(view -> selectImage());
        buttonUpdate.setOnClickListener(view -> updateDoctor());

        Button btnBackUpdate = findViewById(R.id.btnBackUpdate);
        btnBackUpdate.setOnClickListener(v -> {
            Toast.makeText(UpdateDoctorActivity.this, "Going Back", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateDoctorActivity.this, AdminHomeActivity.class);
            startActivity(intent);
        });
    }

    private void searchDoctor() {
        String doctorName = editTextName.getText().toString().trim();
        if (doctorName.isEmpty()) {
            Toast.makeText(this, "Please enter a doctor's name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getDoctorByName(doctorName);
        if (cursor != null && cursor.moveToFirst()) {
            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_ID));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE));
            double fee = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FEE));
            String visitingTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VISITING_TIME));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_IMAGE_URI));

            // Set data to UI
            editTextSpecialization.setText(specialization);
            editTextPhone.setText(phone);
            editTextFee.setText(String.valueOf(fee));
            editTextVisitingTime.setText(visitingTime);
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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewDoctor.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                doctorImageByteArray = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDoctor() {
        String doctorName = editTextName.getText().toString().trim();
        String specialization = editTextSpecialization.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String fee = editTextFee.getText().toString().trim();
        String visitingTime = editTextVisitingTime.getText().toString().trim();

        if (doctorName.isEmpty() || specialization.isEmpty() || phone.isEmpty() || fee.isEmpty() || visitingTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int doctorId = Integer.parseInt(textViewDoctorId.getText().toString().replaceAll("\\D+", ""));
        double feeValue = Double.parseDouble(fee);

        if (doctorImageByteArray == null) {
            // Fetch existing image if not updated
            Cursor cursor = databaseHelper.getDoctorByName(doctorName);
            if (cursor != null && cursor.moveToFirst()) {
                doctorImageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_IMAGE_URI));
                cursor.close();
            }
        }

        boolean isUpdated = databaseHelper.updateDoctor(doctorId, doctorName, specialization, phone, feeValue, visitingTime, doctorImageByteArray);

        if (isUpdated) {
            Toast.makeText(this, "Doctor updated successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        editTextSpecialization.setText("");
        editTextPhone.setText("");
        editTextFee.setText("");
        editTextVisitingTime.setText("");
        textViewDoctorId.setText("");
        imageViewDoctor.setImageResource(0); // Clear image view
        doctorImageByteArray = null;
    }
}
