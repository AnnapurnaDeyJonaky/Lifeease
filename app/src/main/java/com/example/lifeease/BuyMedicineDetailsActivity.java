package com.example.lifeease;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BuyMedicineDetailsActivity extends AppCompatActivity {

    TextView tvProductName, tvTotalCost;
    EditText edQuantity;
    Button btnBack, btnAddToCart;

    private float productPrice; // Ensure this is a float

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_details);

        // Initializing FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Initializing views
        tvProductName = findViewById(R.id.textViewBMDPackageName);
        tvTotalCost = findViewById(R.id.textViewTotalCost);
        edQuantity = findViewById(R.id.editTextQuantity);
        btnBack = findViewById(R.id.btnBackMedDetails);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Receiving data from the intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        productPrice = intent.getFloatExtra("productPrice", 0); // Ensure it's retrieved as a float
        productPrice=productPrice/10;

        // Setting values to views
        tvProductName.setText(productName);
        tvTotalCost.setText("Total Cost: " + productPrice + "/=");

        // Dynamically update total cost
        edQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotalCost();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Back Button
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backIntent);
        });

        // Add to Cart Button
        btnAddToCart.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                // User is not logged in
                Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show();
                // Redirect to Login Activity
                Intent loginIntent = new Intent(this, MainActivity.class);
                startActivity(loginIntent);
                return;
            }

            String username = currentUser.getEmail(); // Using email as username or set another identifier
            String quantityStr = edQuantity.getText().toString();
            if (quantityStr.isEmpty()) {
                Toast.makeText(this, "Please enter the quantity.", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            float totalCost = quantity * productPrice; // Ensure price is multiplied correctly

            DatabaseHelper db = new DatabaseHelper(this);

            if(quantity>50){
                Toast.makeText(this, "You can't add more than 50 tablets", Toast.LENGTH_SHORT).show();
            }
            else if (db.checkCart(username, productName)) {
                Toast.makeText(this, "Product already added", Toast.LENGTH_SHORT).show();
            } else {
                db.addCart(username, productName, totalCost, quantity);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();

                // Redirect back to product listing or cart activity
                Intent backIntent = new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class);
                backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backIntent);
            }
        });
    }

    // Update the total cost dynamically
    private void updateTotalCost() {
        String quantityStr = edQuantity.getText().toString();
        if (!quantityStr.isEmpty()) {
            int quantity = Integer.parseInt(quantityStr);
            float totalCost = quantity * productPrice;
            tvTotalCost.setText("Total Cost: " + totalCost + "/=");
        } else {
            tvTotalCost.setText("Total Cost: 0.0/=");
        }
    }
}
