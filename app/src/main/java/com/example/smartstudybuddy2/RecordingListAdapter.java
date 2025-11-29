package com.example.smartstudybuddy2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordingListAdapter extends BaseAdapter {

    public interface OnRecordingClickListener {
        void onPlayClicked(Recording recording);
        void onDeleteClicked(Recording recording);
    }

    private Context context;
    private ArrayList<Recording> recordings;
    private OnRecordingClickListener listener;

    public RecordingListAdapter(Context context, ArrayList<Recording> recordings, OnRecordingClickListener listener) {
        this.context = context;
        this.recordings = recordings;
        this.listener = listener;
    }

    @Override
    public int getCount() { return recordings.size(); }

    @Override
    public Object getItem(int position) { return recordings.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recording, parent, false);

        Recording recording = recordings.get(position);

        TextView nameText = convertView.findViewById(R.id.tvRecordingName);
        TextView dateText = convertView.findViewById(R.id.tvRecordingDate);
        ImageButton playButton = convertView.findViewById(R.id.btnPlayRecording);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDeleteRecording);

        nameText.setText(recording.getFileName());
        dateText.setText(recording.getDate());

        playButton.setOnClickListener(v -> listener.onPlayClicked(recording));
        deleteButton.setOnClickListener(v -> listener.onDeleteClicked(recording));

        return convertView;
    }
}
