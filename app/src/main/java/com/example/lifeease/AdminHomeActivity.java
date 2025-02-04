package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Button btn_insert_product = findViewById(R.id.btn_insert_product);
        Button btnViewProduct = findViewById(R.id.btn_view_product);
        Button btnUpdateProduct = findViewById(R.id.btn_update_product_admin);
        Button btnDeleteProduct = findViewById(R.id.btn_delete_product_admin);

        Button btnInsertDoctor = findViewById(R.id.btn_insert_doctor);
        Button btnViewDoctor = findViewById(R.id.btn_view_doctor);
        Button btnUpdateDoctor = findViewById(R.id.btn_update_doctor_admin);
        Button btnDeleteDoctor = findViewById(R.id.btn_delete_doctor_admin);

        Button btnLogout = findViewById(R.id.btn_logout_admin);



        btn_insert_product.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,InsertProductActivity.class);
            startActivity(intent);

        });

        btnViewProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,ViewProductActivity.class);
            startActivity(intent);

        });

        btnUpdateProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,UpdateProductActivity.class);
            startActivity(intent);

        });
        btnDeleteProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,DeleteProductActivity.class);
            startActivity(intent);

        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,MainActivity.class);
            startActivity(intent);

        });


        btnInsertDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,InsertDoctor.class);
            startActivity(intent);

        });

        btnViewDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,ViewDoctorActivity.class);
            startActivity(intent);

        });

        btnUpdateDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,UpdateDoctorActivity.class);
            startActivity(intent);

        });

        btnDeleteDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this,DeleteDoctorActivity.class);
            startActivity(intent);

        });


    }
}