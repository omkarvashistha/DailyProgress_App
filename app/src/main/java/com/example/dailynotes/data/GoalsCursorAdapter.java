package com.example.dailynotes.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailynotes.data.NotesContract.GoalsContentEntry;

import com.example.dailynotes.R;

public class GoalsCursorAdapter extends CursorAdapter {

    Context currContext;

    public GoalsCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
        currContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.goals_layout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int statusColumn = cursor.getColumnIndex(GoalsContentEntry.GOALS_STATUS);
        String statusText = cursor.getString(statusColumn);
        int idColumn = cursor.getColumnIndex(GoalsContentEntry._ID);
        int titleColumn = cursor.getColumnIndex(GoalsContentEntry.GOALS_TITLE);
        int descColumn = cursor.getColumnIndex(GoalsContentEntry.GOALS_DESCRIPTION);
        int dateColumn = cursor.getColumnIndex(GoalsContentEntry.GOALS_Date);

        String IdText = cursor.getString(idColumn);
        String titleText = cursor.getString(titleColumn);
        String dateText = cursor.getString(dateColumn);

        TextView Id = view.findViewById(R.id.idTextView);
        TextView Goal = view.findViewById(R.id.Goal_title);
        TextView Added_Date = view.findViewById(R.id.goal_added_date);
        TextView Status = view.findViewById(R.id.goal_status);
        Button mark = view.findViewById(R.id.mark_complete_btn);

        Id.setText(IdText);
        Goal.setText(titleText);
        Added_Date.setText(dateText);
        Status.setText(statusText);
        Status.setTextColor(Color.RED);
        mark.setVisibility(View.VISIBLE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);
        final TextView IdTexView = (TextView) view.findViewById(R.id.idTextView);
        Button mark = view.findViewById(R.id.mark_complete_btn);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(currContext, "Button clicked", Toast.LENGTH_SHORT).show();
                String id = IdTexView.getText().toString();
                Log.e("error", id);
                long _id = Long.parseLong(id);

                Uri curUri = ContentUris.withAppendedId(NotesContract.GoalsContentEntry.CONTENT_URI,_id);
                String updatedText = "Completed";
                ContentValues contentValues = new ContentValues();
                contentValues.put(NotesContract.GoalsContentEntry.GOALS_STATUS,updatedText);

                try{
                    int rowsAffected = currContext.getContentResolver().update(curUri,contentValues,null,null);
                    if(rowsAffected!=0){
                        Toast.makeText(currContext, "Goal Completed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(currContext, "Goal Not Completed", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e("error", e.getMessage().toString());
                    Toast.makeText(currContext, "Exception ocurred", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }

}
