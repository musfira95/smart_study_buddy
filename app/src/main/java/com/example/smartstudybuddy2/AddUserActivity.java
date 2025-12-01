package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.button.MaterialButton;

public class AddUserActivity extends AppCompatActivity {

    TextInputEditText emailEditText, usernameEditText, passwordEditText;
    TextInputLayout emailLayout, usernameLayout, passwordLayout, roleLayout;
    MaterialAutoCompleteTextView roleSpinner;
    MaterialButton addUserButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbHelper = new DatabaseHelper(this);

        // Text fields and layouts
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        emailLayout = findViewById(R.id.emailLayout);
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        roleLayout = findViewById(R.id.roleLayout);

        // Role dropdown (MaterialAutoCompleteTextView)
        roleSpinner = findViewById(R.id.roleSpinner);
        addUserButton = findViewById(R.id.addUserButton);

        // Role options
        String[] roles = {"User", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemClickListener((parent, view, position, id) -> {
            // clear any role error when user selects
            roleLayout.setError(null);
            roleLayout.setErrorEnabled(false);
        });

        addUserButton.setOnClickListener(v -> {
            // clear previous errors
            usernameLayout.setError(null);
            emailLayout.setError(null);
            passwordLayout.setError(null);
            roleLayout.setError(null);

            String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
            String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
            String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";
            String role = roleSpinner.getText() != null ? roleSpinner.getText().toString().trim() : "";

            boolean valid = true;

            if (username.isEmpty()) {
                usernameLayout.setError("Username is required");
                valid = false;
            }

            if (email.isEmpty()) {
                emailLayout.setError("Email is required");
                valid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.setError("Enter a valid email");
                valid = false;
            }

            if (password.isEmpty()) {
                passwordLayout.setError("Password is required");
                valid = false;
            } else if (password.length() < 6) {
                passwordLayout.setError("Password must be at least 6 characters");
                valid = false;
            }

            if (role.isEmpty()) {
                roleLayout.setError("Please select a role");
                valid = false;
            }

            if (!valid) {
                // focus first error field for convenience
                if (usernameLayout.getError() != null) {
                    usernameEditText.requestFocus();
                } else if (emailLayout.getError() != null) {
                    emailEditText.requestFocus();
                } else if (passwordLayout.getError() != null) {
                    passwordEditText.requestFocus();
                } else if (roleLayout.getError() != null) {
                    roleSpinner.requestFocus();
                }
                return;
            }

            boolean inserted = dbHelper.insertUser(email, username, password, role);
            if(inserted){
                Toast.makeText(AddUserActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                // Open Users list so admin can see/manage users
                startActivity(new android.content.Intent(AddUserActivity.this, UsersListActivity.class));
                finish();
             } else {
                 // Show a friendly error on email (assuming unique constraint on email)
                 emailLayout.setError("User already exists");
             }
        });
    }
}
