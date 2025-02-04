package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckoutActivity extends AppCompatActivity {

    private EditText editTextLocation;
    private TextView textViewMessage, textDeliveryCharge;
    private Button btnConfirmOrder;

    private String[] freeDeliveryAreas = {"Zindabazar", "Subidbazar", "Mirabazar"};
    private float totalCost, deliveryCharge;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Get the total cost from the intent
        totalCost = getIntent().getFloatExtra("total_cost", 0);

        // Find views
        editTextLocation = findViewById(R.id.editTextLocation);
        textViewMessage = findViewById(R.id.text_view_message);
        textDeliveryCharge = findViewById(R.id.text_delivery_charge);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Display the initial message without delivery charge
        textViewMessage.setText("Your order has been placed successfully!\nTotal Cost: " + totalCost + "/=\nCash On Delivery!\n Thank you for your order!");

        // Set a default message for delivery charge
        textDeliveryCharge.setText("Delivery Charge: Calculating...");

        // Button click listener to confirm order
        btnConfirmOrder.setOnClickListener(v -> confirmOrder());
    }

    private void confirmOrder() {
        // Get custom delivery location entered by the user
        String customLocation = editTextLocation.getText().toString().trim();

        if (customLocation.isEmpty()) {
            Toast.makeText(this, "Please enter a delivery location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique order ID (you can use System.currentTimeMillis() or Firebase Database's auto-generated IDs)
        String orderId = "ORDER" + System.currentTimeMillis(); // Order ID based on current time

        // Calculate the delivery charge based on the location
        if (isFreeDeliveryArea(customLocation)) {
            deliveryCharge = 0;
            textDeliveryCharge.setText("Delivery Charge: Free");
        } else {
            deliveryCharge = 100; // Example delivery charge, adjust based on your requirement
            textDeliveryCharge.setText("Delivery Charge: " + deliveryCharge + "/=");
        }

        // Update the total cost after adding delivery charge
        float finalTotalCost = totalCost + deliveryCharge;

        // Get user info (name, phone number)
        String userName = currentUser != null ? currentUser.getDisplayName() : "Unknown";
        String userPhone = currentUser != null ? currentUser.getPhoneNumber() : "Unknown";
        String userLocation = customLocation;

        // Create invoice data
        String invoiceMessage = "Invoice:\n\nOrder ID: " + orderId + // Include the order ID
                "\nName: " + userName +
                "\nPhone: " + userPhone +
                "\nLocation: " + userLocation +
                "\nTotal Cost: " + finalTotalCost + "/=";

        // Display the invoice in a new activity or dialog
        Intent invoiceIntent = new Intent(CheckoutActivity.this, InvoiceActivity.class);
        invoiceIntent.putExtra("invoice_message", invoiceMessage);
        startActivity(invoiceIntent);
    }

    private boolean isFreeDeliveryArea(String location) {
        for (String area : freeDeliveryAreas) {
            if (area.equalsIgnoreCase(location)) {
                return true; // Free delivery area
            }
        }
        return false; // Non-free delivery area
    }
}