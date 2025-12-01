package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class EditUserActivity extends AppCompatActivity {

    TextInputEditText usernameEditText, emailEditText, passwordEditText;
    TextInputLayout usernameLayout, emailLayout, passwordLayout, roleLayout;
    MaterialAutoCompleteTextView roleSpinner;
    MaterialButton saveBtn, cancelBtn;
    DatabaseHelper dbHelper;

    String originalEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        roleLayout = findViewById(R.id.roleLayout);

        roleSpinner = findViewById(R.id.roleSpinner);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        // Roles
        String[] roles = new String[]{"User", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        roleSpinner.setAdapter(adapter);

        // Pre-fill from intent extras
        Intent intent = getIntent();
        originalEmail = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");
        String password = intent.getStringExtra("password");

        if (originalEmail != null) emailEditText.setText(originalEmail);
        if (username != null) usernameEditText.setText(username);
        if (password != null) passwordEditText.setText(""); // keep empty: optional
        if (role != null) {
            // show capitalized role in dropdown
            roleSpinner.setText(role.substring(0,1).toUpperCase(Locale.ROOT) + role.substring(1));
        }

        saveBtn.setOnClickListener(v -> {
            // clear errors
            usernameLayout.setError(null);
            emailLayout.setError(null);
            passwordLayout.setError(null);
            roleLayout.setError(null);

            String newUsername = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
            String newEmail = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
            String newPassword = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";
            String newRole = roleSpinner.getText() != null ? roleSpinner.getText().toString().trim() : "";

            boolean valid = true;
            if (newUsername.isEmpty()) {
                usernameLayout.setError("Username is required");
                valid = false;
            }
            if (newEmail.isEmpty()) {
                emailLayout.setError("Email is required");
                valid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                emailLayout.setError("Enter a valid email");
                valid = false;
            } else if (!newEmail.equalsIgnoreCase(originalEmail) && dbHelper.checkEmailExists(newEmail)) {
                emailLayout.setError("Email already in use");
                valid = false;
            }

            if (!newPassword.isEmpty() && newPassword.length() < 6) {
                passwordLayout.setError("Password must be at least 6 characters");
                valid = false;
            }

            if (newRole.isEmpty()) {
                roleLayout.setError("Please select a role");
                valid = false;
            }

            if (!valid) return;

            // If password empty, keep the existing password from DB
            String finalPassword = newPassword;
            if (finalPassword.isEmpty()) {
                // fetch existing password via getAllUsers cursor
                android.database.Cursor c = dbHelper.getAllUsers();
                String existingPwd = null;
                if (c.moveToFirst()) {
                    do {
                        String e = c.getString(1); // email column
                        if (e.equalsIgnoreCase(originalEmail)) {
                            existingPwd = c.getString(3); // password column
                            break;
                        }
                    } while (c.moveToNext());
                }
                c.close();
                finalPassword = existingPwd != null ? existingPwd : "";
            }

            String normalizedRole = newRole.toLowerCase(Locale.ROOT);

            boolean updated = dbHelper.updateUser(originalEmail, newEmail, newUsername, finalPassword, normalizedRole);
            if (updated) {
                Toast.makeText(EditUserActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(EditUserActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(v -> finish());
    }
}
