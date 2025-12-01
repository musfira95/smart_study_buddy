package com.example.smartstudybuddy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    TextView tvEmpty;
    DatabaseHelper dbHelper;
    ArrayList<UserModel> usersList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        tvEmpty = findViewById(R.id.tvEmptyUsers);
        dbHelper = new DatabaseHelper(this);

        loadUsers();
    }

    private void loadUsers() {
        usersList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllUsers();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int emailIndex = cursor.getColumnIndex("email");
                int usernameIndex = cursor.getColumnIndex("username");
                int roleIndex = cursor.getColumnIndex("role");
                int passwordIndex = cursor.getColumnIndex("password");

                String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "";
                String username = (usernameIndex != -1) ? cursor.getString(usernameIndex) : "";
                String role = (roleIndex != -1) ? cursor.getString(roleIndex) : "user";
                String password = (passwordIndex != -1) ? cursor.getString(passwordIndex) : "";

                usersList.add(new UserModel(username, email, role, password));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (usersList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            usersRecyclerView.setVisibility(View.GONE);
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        usersRecyclerView.setVisibility(View.VISIBLE);
        adapter = new UserAdapter(this, usersList, dbHelper);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }
}

