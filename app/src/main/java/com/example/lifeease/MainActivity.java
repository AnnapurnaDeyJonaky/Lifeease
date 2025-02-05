package com.example.lifeease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                fetchUserData(currentUser.getUid()); // ✅ Fetch User Name & Phone
            } else {
                auth.signOut();
                Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
            }
            return;
        }

        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String usernameOrEmail = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (usernameOrEmail.equals("admin") && password.equals("admin")) {
                startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
                return;
            }

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()) {
                loginUser(usernameOrEmail, password);
            } else {
                db.collection("users")
                        .whereEqualTo("username", usernameOrEmail)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                String email = queryDocumentSnapshots.getDocuments().get(0).getString("email");
                                loginUser(email, password);
                            } else {
                                Toast.makeText(MainActivity.this, "Username not found!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            fetchUserData(user.getUid()); // ✅ Fetch User Data
                        } else {
                            auth.signOut();
                            Toast.makeText(MainActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserData(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String phone = documentSnapshot.getString("phone");

                        UserData.setUsername(username);
                        UserData.setPhone(phone);

                        Toast.makeText(MainActivity.this, "Login successful! Welcome, " + username, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ProductsDisplay.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error fetching user data!", Toast.LENGTH_SHORT).show());
    }
}