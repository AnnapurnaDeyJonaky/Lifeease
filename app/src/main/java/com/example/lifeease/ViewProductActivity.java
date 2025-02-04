package com.example.lifeease;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ViewProductActivity extends AppCompatActivity {
    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        listViewProducts = findViewById(R.id.list_view_products);
        Button buttonBack = findViewById(R.id.button_back);
        databaseHelper = new DatabaseHelper(this);

        displayProducts();

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(ViewProductActivity.this, AdminHomeActivity.class);
            startActivity(intent);
        });

        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            if (cursor != null && cursor.moveToPosition(position)) {
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
                Toast.makeText(this, "Selected Product: " + productName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ViewProductActivity.this, UpdateProductActivity.class);
                intent.putExtra("PRODUCT_ID", productId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Unable to retrieve product details.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the displayed products
        displayProducts();
    }

    private void displayProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
            return;
        }
        ProductAdapter adapter = new ProductAdapter(this, cursor, 0);
        listViewProducts.setAdapter(adapter);
    }
}
