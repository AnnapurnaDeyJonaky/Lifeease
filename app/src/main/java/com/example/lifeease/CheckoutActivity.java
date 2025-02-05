package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckoutActivity extends AppCompatActivity {

    private EditText editTextAdditionalAddress;
    private TextView textViewMessage, textDeliveryCharge;
    private Button btnConfirmOrder;
    private Spinner spinnerLocation;

    // Free delivery areas
    private String[] freeDeliveryAreas = {
            "Zindabazar", "Chouhatta", "Rikabibazar", "Bandar", "Lamabazar", "Nayasarak"
    };

    // Chargeable areas (delivery charge 120 Taka)
    private String[] chargeableAreas = {
            "Subidbazar", "Mirabazar", "Shibganj", "Nabiganj", "Bagbari", "Madrasa Road",
            "Shahjalal Upojela", "Osmaninagar", "Khadimnagar", "Chhoygaon", "Surma", "Upojela Road", "Patai"
    };

    private String[] allAreas; // Combined list of all areas for the spinner

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
        editTextAdditionalAddress = findViewById(R.id.editTextAdditionalAddress);
        textViewMessage = findViewById(R.id.text_view_message);
        textDeliveryCharge = findViewById(R.id.text_delivery_charge);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        spinnerLocation = findViewById(R.id.spinnerLocation);

        // Combine the free and chargeable areas into one array for the spinner
        allAreas = new String[freeDeliveryAreas.length + chargeableAreas.length];
        System.arraycopy(freeDeliveryAreas, 0, allAreas, 0, freeDeliveryAreas.length);
        System.arraycopy(chargeableAreas, 0, allAreas, freeDeliveryAreas.length, chargeableAreas.length);

        // Set up the spinner for location selection
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allAreas);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        // Button click listener to confirm order
        btnConfirmOrder.setOnClickListener(v -> confirmOrder());

        // Spinner item selected listener
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLocation = (String) parentView.getItemAtPosition(position);
                if (isFreeDeliveryLocation(selectedLocation)) {
                    deliveryCharge = 0; // Free delivery
                    textDeliveryCharge.setText("Delivery Charge: Free");
                } else {
                    deliveryCharge = 120; // 120 Taka delivery charge
                    textDeliveryCharge.setText("Delivery Charge: " + deliveryCharge + " Taka");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action required
            }
        });

        // Display the initial message without delivery charge
        textViewMessage.setText("Your order has been placed successfully!\nTotal Cost: " + totalCost + "/=\nCash On Delivery!\n Thank you for your order!");
    }

    private void confirmOrder() {
        // Get custom address entered by the user (additional address like house number, road number)
        String additionalAddress = editTextAdditionalAddress.getText().toString().trim();

        if (additionalAddress.isEmpty()) {
            Toast.makeText(this, "Please enter your additional address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique order ID
        String orderId = "ORDER" + System.currentTimeMillis(); // Order ID based on current time

        // Update the total cost after adding the delivery charge
        float finalTotalCost = totalCost + deliveryCharge;

        // Get user info (name, phone number)
        String userName = currentUser != null ? currentUser.getDisplayName() : "Unknown";
        String userPhone = currentUser != null ? currentUser.getPhoneNumber() : "Unknown";
        String userLocation = (String) spinnerLocation.getSelectedItem();

        // Create invoice data
        String invoiceMessage = "Invoice:\n\nOrder ID: " + orderId + "\nName: " + userName +
                "\nPhone: " + userPhone + "\nLocation: " + userLocation + "\nAdditional Address: " + additionalAddress +
                "\nTotal Cost: " + finalTotalCost + "/=";

        // Display the invoice in a new activity
        Intent invoiceIntent = new Intent(CheckoutActivity.this, InvoiceActivity.class);
        invoiceIntent.putExtra("invoice_message", invoiceMessage);
        startActivity(invoiceIntent);
    }

    private boolean isFreeDeliveryLocation(String location) {
        for (String area : freeDeliveryAreas) {
            if (location.equals(area)) {
                return true; // Free delivery location
            }
        }
        return false; // Delivery chargeable location
    }
}
