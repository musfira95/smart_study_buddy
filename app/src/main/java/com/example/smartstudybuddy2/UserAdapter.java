package com.example.smartstudybuddy2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    ArrayList<UserModel> usersList;
    DatabaseHelper dbHelper;

    public UserAdapter(Context context, ArrayList<UserModel> usersList, DatabaseHelper dbHelper) {
        this.context = context;
        this.usersList = usersList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = usersList.get(position);

        String usernameLabel = context.getString(R.string.username_hint) + ": " + user.getUsername();
        String emailLabel = context.getString(R.string.email_hint) + ": " + user.getEmail();
        String roleLabel = context.getString(R.string.role_hint) + ": " + user.getRole();

        holder.usernameText.setText(usernameLabel);
        holder.emailText.setText(emailLabel);
        holder.roleText.setText(roleLabel);

        // --- Edit Button ---
        holder.editBtn.setOnClickListener(v -> {
            // Launch EditUserActivity with extras
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("email", user.getEmail());
            intent.putExtra("username", user.getUsername());
            intent.putExtra("role", user.getRole());
            intent.putExtra("password", user.getPassword());
            context.startActivity(intent);
        });

        // --- Delete Button ---
        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete " + user.getUsername() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Prevent deleting last admin
                        if (user.getRole().equalsIgnoreCase("admin") && dbHelper.isLastAdmin(user.getEmail())) {
                            Toast.makeText(context, "Cannot delete the last admin", Toast.LENGTH_LONG).show();
                            return;
                        }

                        boolean deleted = dbHelper.deleteUser(user.getEmail());
                        if (deleted) {
                            usersList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // --- Manage Role Button ---
        holder.manageRoleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, RoleManagementActivity.class);
            intent.putExtra("email", user.getEmail());
            intent.putExtra("username", user.getUsername());
            intent.putExtra("role", user.getRole());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText, emailText, roleText;
        Button editBtn, deleteBtn, manageRoleBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.usernameText);
            emailText = itemView.findViewById(R.id.emailText);
            roleText = itemView.findViewById(R.id.roleText);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            manageRoleBtn = itemView.findViewById(R.id.manageRoleBtn);
        }
    }
}
