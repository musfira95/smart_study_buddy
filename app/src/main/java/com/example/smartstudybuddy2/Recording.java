package com.example.smartstudybuddy2;

public class Recording {
    private int id;
    private String fileName;
    private String filePath;
    private String date;

    public Recording(int id, String fileName, String filePath, String date) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.date = date;
    }

    public int getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getDate() { return date; }
}
