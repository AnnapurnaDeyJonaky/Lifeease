package com.example.lifeease;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertDoctor extends AppCompatActivity {

    private EditText doctorNameEditText;
    private EditText specializationEditText;
    private EditText phoneEditText;
    private EditText feeEditText;
    private EditText visitingTimeEditText;
    private ImageView selectedImageView;
    private Button selectImageButton;
    private Button insertDoctorButton;
    private DatabaseHelper databaseHelper;
    private byte[] imageByteArray;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_doctor);

        // Initialize UI components
        doctorNameEditText = findViewById(R.id.et_doctor_name);
        specializationEditText = findViewById(R.id.et_specialization);
        phoneEditText = findViewById(R.id.et_phone);
        feeEditText = findViewById(R.id.et_fee);
        visitingTimeEditText = findViewById(R.id.et_visiting_time);
        selectedImageView = findViewById(R.id.iv_selected_image);
        selectImageButton = findViewById(R.id.btn_select_image);
        insertDoctorButton = findViewById(R.id.btn_insert_doctor);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    selectedImageView.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Show image picker when select image button is clicked
        selectImageButton.setOnClickListener(view -> showImageSelectionDialog());

        // Insert doctor into the database when insert button is clicked
        insertDoctorButton.setOnClickListener(view -> insertDoctor());
    }

    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void insertDoctor() {
        String name = doctorNameEditText.getText().toString();
        String specialization = specializationEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String feeStr = feeEditText.getText().toString();
        String visitingTime = visitingTimeEditText.getText().toString();

        // Validate input fields
        if (name.isEmpty() || specialization.isEmpty() || phone.isEmpty() || feeStr.isEmpty() || visitingTime.isEmpty() || imageByteArray == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double fee;
        try {
            fee = Double.parseDouble(feeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid fee", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert doctor into the database
        boolean isInserted = databaseHelper.insertDoctor(name, specialization, phone, fee, visitingTime, imageByteArray);
        if (isInserted) {
            Toast.makeText(this, "Doctor inserted successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to insert doctor", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        doctorNameEditText.setText("");
        specializationEditText.setText("");
        phoneEditText.setText("");
        feeEditText.setText("");
        visitingTimeEditText.setText("");
        selectedImageView.setImageResource(0);
    }
}
