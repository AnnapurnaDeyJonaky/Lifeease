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

        // Get user data from UserData class
        String userName = UserData.getUsername(); // User name fetch
        String userPhone = UserData.getPhone();   // User phone fetch

        // Set user details in TextViews
        textViewUserName.setText("Name: " + (userName != null ? userName : "N/A"));
        textViewUserPhone.setText("Phone: " + (userPhone != null ? userPhone : "N/A"));

        // Retrieve the invoice message from the intent
        Intent intent = getIntent();
        String invoiceMessage = intent.getStringExtra("invoice_message");

        // Extract Order ID, Location, Additional Address, and Total Cost
        String formattedInvoice = extractInvoiceDetails(invoiceMessage);

        // Set formatted invoice details to TextView
        textViewInvoiceDetails.setText(formattedInvoice);

        // Button click listener to go back to home
        buttonBackToHome.setOnClickListener(v -> finish()); // Finish the activity and go back
    }

    private String extractInvoiceDetails(String invoiceMessage) {
        if (invoiceMessage == null || invoiceMessage.isEmpty()) {
            return "No invoice details available.";
        }

        String orderId = "", location = "", additionalAddress = "", totalCost = "";
        String[] lines = invoiceMessage.split("\n");

        // Extract relevant details from invoice message
        for (String line : lines) {
            if (line.startsWith("Order ID:")) {
                orderId = line;
            } else if (line.startsWith("Location:")) {
                location = line.replace("Location: ", ""); // Remove the "Location: " part
            } else if (line.startsWith("Additional Address:")) {
                additionalAddress = line.replace("Additional Address: ", ""); // Remove the "Additional Address: " part
            } else if (line.startsWith("Total Cost:")) {
                totalCost = line;
            }
        }

        // Format the location to include both area and additional address
        if (!location.isEmpty() && !additionalAddress.isEmpty()) {
            location = location + ", " + additionalAddress;
        } else if (additionalAddress.isEmpty()) {
            location = location;  // Only show the area if no additional address is provided
        }

        // Return the formatted invoice
        return orderId + "\nLocation: " + location + "\n" + totalCost;
    }
}
