package com.example.lifeease;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;

public class BuyMedicineActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lst;
    private Button btnGoToCart;
    private SearchView searchView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);

        dbHelper = new DatabaseHelper(this);
        lst = findViewById(R.id.buyMedicinesListView);
        btnGoToCart = findViewById(R.id.btnCart);
        searchView = findViewById(R.id.medicineSearchView);

        Button back = findViewById(R.id.backButtonBuyMedicine);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(BuyMedicineActivity.this, ProductsDisplay.class);
            startActivity(intent);
            finish(); // Ensures the current activity is properly cleaned up
        });

        btnGoToCart.setOnClickListener(v -> {
            Intent intent = new Intent(BuyMedicineActivity.this, cartBuyMedicineActivity.class);
            startActivity(intent);
        });

        displayProducts();

        lst.setOnItemClickListener((parent, view, position, id) -> {
            try {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null && cursor.moveToPosition(position)) {
                    String productName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_NAME));
                    float productPrice = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_PRODUCT_PRICE));

                    Intent intent = new Intent(BuyMedicineActivity.this, BuyMedicineDetailsActivity.class);
                    intent.putExtra("productName", productName);
                    intent.putExtra("productPrice", productPrice);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Unable to retrieve product details.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("BuyMedicineActivity", "Error retrieving item details", e);
                Toast.makeText(this, "Error retrieving item details.", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query); // Call handleSearch on submit
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMedicine(newText); // Update search results dynamically without closing the keyboard
                return true;
            }
        });

    }

    private void handleSearch(String query) {
        searchMedicine(query);
    }

    private void displayProducts() {
        try {
            Cursor cursor = dbHelper.getAllProducts();
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
                return;
            }
            if (adapter == null) {
                adapter = new ProductAdapter(this, cursor, 0);
                lst.setAdapter(adapter);
            } else {
                adapter.changeCursor(cursor);
            }
        } catch (Exception e) {
            Log.e("BuyMedicineActivity", "Error displaying products", e);
            Toast.makeText(this, "Error loading products.", Toast.LENGTH_SHORT).show();
        }
    }


    private void searchMedicine(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                displayProducts();
                return;
            }

            // Perform case-insensitive search in the database
            Cursor cursor = dbHelper.getSearchProductByName(query);
            if (cursor != null && cursor.getCount() > 0) {
                if (adapter == null) {
                    adapter = new ProductAdapter(this, cursor, 0);
                    lst.setAdapter(adapter);
                } else {
                    adapter.changeCursor(cursor);
                }
            } else {
                Toast.makeText(this, "No medicines found matching the query.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("BuyMedicineActivity", "Error during search", e);
            Toast.makeText(this, "Error searching for medicines.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }
}
