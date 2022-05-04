package com.example.dailynotes.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailynotes.data.NotesContract.NotesContentEntry;

import com.example.dailynotes.R;

public class NotesCursorAdapter extends CursorAdapter {

    Context currContext;

    public NotesCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
        currContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notes_list_view,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView Title = view.findViewById(R.id.Note_title);
        TextView date = view.findViewById(R.id.list_item_date);
        TextView time = view.findViewById(R.id.list_item_time);

        int idColumn = cursor.getColumnIndex(NotesContentEntry._ID);
        int titleColumn = cursor.getColumnIndex(NotesContentEntry.COLUMN_TITLE);
        int contentColumn = cursor.getColumnIndex(NotesContentEntry.COLUMN_NOTE_TEXT);
        int dateColumn = cursor.getColumnIndex(NotesContentEntry.COLUMN_Date);
        int timeColumn = cursor.getColumnIndex(NotesContentEntry.COLUMN_Time);


        String titleText = cursor.getString(titleColumn);
        String dateText = cursor.getString(dateColumn);
        String timeText = cursor.getString(timeColumn);


        Title.setText(titleText);
        date.setText(dateText);
        time.setText(timeText);

    }


}
