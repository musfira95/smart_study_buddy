package com.example.smartstudybuddy2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SmartStudyBuddy.db";
    private static final int DB_VERSION = 7; // incremented because table changed

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ✅ Create Users table
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "username TEXT, " +
                "password TEXT, " +
                "role TEXT DEFAULT 'user')");

        // ✅ Default admin account
        db.execSQL("INSERT INTO Users (email, username, password, role) " +
                "VALUES ('musfira@gmail.com', 'Musfira', '123456', 'admin')");

        // ✅ Create Audio Files table (with date column)
        db.execSQL("CREATE TABLE IF NOT EXISTS audio_files (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "file_name TEXT, " +
                "file_path TEXT, " +
                "date TEXT)");

        // ✅ Create Notes table
        db.execSQL("CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS audio_files");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    // ✅ Insert new user with role
    public boolean insertUser(String email, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("role", role);
        long result = db.insert("Users", null, cv);
        db.close();
        return result != -1;
    }

    // ✅ Check login credentials
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // ✅ Get user role
    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM Users WHERE email=?", new String[]{email});
        String role = "user";
        if (cursor != null && cursor.moveToFirst()) {
            role = cursor.getString(0);
            cursor.close();
        }
        db.close();
        return role;
    }

    // ✅ Check if email already exists
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE email=?", new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // ✅ Update password
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", newPassword);
        int rows = db.update("Users", cv, "email=?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // ✅ Get all users (for Admin Dashboard)
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT username, email, role, password FROM Users", null);
    }

    // ✅ Update user role
    public boolean updateUserRole(String email, String newRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("role", newRole);
        int rows = db.update("Users", cv, "email=?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // ✅ Update user info
    public boolean updateUser(String oldEmail, String newEmail, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", newEmail);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("role", role);
        int rows = db.update("Users", cv, "email=?", new String[]{oldEmail});
        return rows > 0;
    }

    // ✅ Delete user
    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("Users", "email=?", new String[]{email});
        return rows > 0;
    }

    // New: check if provided email is the last admin
    public boolean isLastAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean result = false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Users WHERE role='admin'", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        // if only one admin exists, verify it's the provided email
        if (count == 1) {
            Cursor c2 = db.rawQuery("SELECT email FROM Users WHERE role='admin' LIMIT 1", null);
            if (c2 != null && c2.moveToFirst()) {
                String adminEmail = c2.getString(0);
                result = adminEmail.equalsIgnoreCase(email);
                c2.close();
            }
        }
        db.close();
        return result;
    }

    // ✅ Insert audio file info (with current date)
    public void insertAudioFile(String fileName, String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("file_name", fileName);
        values.put("file_path", filePath);
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        values.put("date", currentDate);
        db.insert("audio_files", null, values);
        db.close();
    }

    // ✅ Insert new note
    public boolean insertNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        long result = db.insert("notes", null, values);
        db.close();
        return result != -1;
    }

    // ✅ Fetch all saved notes
    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM notes", null);
    }

    // ✅ Add new recording (with date)
    public void addRecording(String fileName, String filePath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("file_name", fileName);
        values.put("file_path", filePath);
        values.put("date", date);
        db.insert("audio_files", null, values);
        db.close();
    }

    // ✅ Fetch all recordings and return as ArrayList
    public ArrayList<Recording> getAllRecordings() {
        ArrayList<Recording> recordings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM audio_files", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String fileName = cursor.getString(cursor.getColumnIndexOrThrow("file_name"));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow("file_path"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                recordings.add(new Recording(id, fileName, filePath, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordings;
    }
    public void insertRecording(String fileName, String filePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("file_name", fileName);
        values.put("file_path", filePath);
        values.put("date", System.currentTimeMillis());

        db.insert("recordings", null, values);
        db.close();
    }


    // ✅ Delete a recording
    public void deleteRecording(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("audio_files", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ✅ Update note
    public boolean updateNote(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        int result = db.update("notes", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // ✅ Delete note
    public boolean deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("notes", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
}
