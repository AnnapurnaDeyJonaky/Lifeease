package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InvoiceActivity extends AppCompatActivity {

    private TextView textViewInvoiceDetails, textViewUserName, textViewUserPhone;
    private Button buttonBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Find views
        textViewInvoiceDetails = findViewById(R.id.textViewInvoiceDetails);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserPhone = findViewById(R.id.textViewUserPhone);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);

        // Retrieve the invoice message from the intent
        Intent intent = getIntent();
        String invoiceMessage = intent.getStringExtra("invoice_message");

        // Get user data from UserData class
        String userName = UserData.getUsername();
        String userPhone = UserData.getPhone();

        // Set user data and invoice details to TextViews
        textViewUserName.setText("Name: " + userName);
        textViewUserPhone.setText("Phone: " + userPhone);
        textViewInvoiceDetails.setText(invoiceMessage);

        // Button click listener to go back to home
        buttonBackToHome.setOnClickListener(v -> {
            finish(); // Finish the activity and go back
        });
    }
}
