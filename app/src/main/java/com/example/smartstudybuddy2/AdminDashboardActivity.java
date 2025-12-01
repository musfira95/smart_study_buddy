package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    Button addUserButton;
    Button viewUsersButton;
    DatabaseHelper dbHelper;
    ArrayList<UserModel> usersList;
    String loggedInEmail;

    Button logoutButton;

    UserAdapter adapter; // ✅ Correct adapter name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DatabaseHelper(this);
        addUserButton = findViewById(R.id.addUserButton);
        viewUsersButton = findViewById(R.id.viewUsersButton);
        // new: view users button
        viewUsersButton.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, UsersListActivity.class)));

        loggedInEmail = getIntent().getStringExtra("email");

        addUserButton.setOnClickListener(v -> startActivity(new android.content.Intent(AdminDashboardActivity.this, AddUserActivity.class)));
        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // admin dashboard close kar dega
        });

    }




    // ✅ Optional (refresh users on screen)
    @Override
    protected void onResume() {
        super.onResume();
    }
}
