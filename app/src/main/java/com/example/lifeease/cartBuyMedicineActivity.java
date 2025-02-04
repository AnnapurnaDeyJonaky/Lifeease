package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class cartBuyMedicineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView textViewTotalCost;
    private Button btnCheckout;
    private DatabaseHelper db;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_buy_medicine);

        recyclerView = findViewById(R.id.recyclerViewCart);
        textViewTotalCost = findViewById(R.id.textViewTotalCost);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Get current user email from FirebaseAuth or session
        currentUserEmail = getCurrentUserEmail();

        if (currentUserEmail == null) {
            Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load cart items from the database
        loadCartItems();

        // Initialize adapter
        cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.CartItemListener() {
            @Override
            public void onDeleteClick(CartItem item) {
                // Remove the item from the database and list, then update the total cost
                db.deleteCartItem(currentUserEmail, item.getProductName());
                cartItems.remove(item);
                cartAdapter.notifyDataSetChanged();
                updateTotalCost();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

// Checkout Button click
        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(cartBuyMedicineActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate total number of medicines and total cost
            int totalMedicines = cartItems.size();
            float totalCost = 0;
            for (CartItem item : cartItems) {
                totalCost += item.getTotalCost();
            }

            // Insert the order into the database
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            boolean orderInserted = db.insertOrder(currentUserEmail, totalMedicines, totalCost, currentDate);

            if (orderInserted) {
                // Clear the cart after order is placed
                cartItems.clear();
                db.clearCart(currentUserEmail); // Add a clearCart method in DatabaseHelper
                cartAdapter.notifyDataSetChanged();
                updateTotalCost();

                // Pass total cost to CheckoutActivity
                Intent checkoutIntent = new Intent(cartBuyMedicineActivity.this, CheckoutActivity.class);
                checkoutIntent.putExtra("total_cost", totalCost);
                startActivity(checkoutIntent);
            } else {
                Toast.makeText(cartBuyMedicineActivity.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


        updateTotalCost();
    }

    // Method to get current user email (adjust based on your Firebase or session logic)
    private String getCurrentUserEmail() {
        // Replace with FirebaseAuth logic if using Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getEmail();
        }
        return null;
    }

    // Method to load cart items for the current user
    private void loadCartItems() {
        cartItems.clear(); // Clear the existing list to avoid duplicates
        List<CartItem> itemsFromDb = db.getCartItems(currentUserEmail);
        if (itemsFromDb != null) {
            cartItems.addAll(itemsFromDb);
        }
    }

    // Method to update the total cost
    private void updateTotalCost() {
        float totalCost = 0;
        for (CartItem item : cartItems) {
            totalCost += item.getTotalCost();
        }
        textViewTotalCost.setText("Total Cost: " + totalCost + "/=");
    }
}
