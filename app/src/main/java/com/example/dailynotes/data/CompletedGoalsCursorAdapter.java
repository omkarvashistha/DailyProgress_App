package com.example.dailynotes.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.dailynotes.R;

public class CompletedGoalsCursorAdapter  extends CursorAdapter  {

    Context currContext;

    public CompletedGoalsCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
        currContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.goals_layout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int statusColumn = cursor.getColumnIndex(NotesContract.GoalsContentEntry.GOALS_STATUS);
        String statusText = cursor.getString(statusColumn);
        Log.d("test", statusText);
        int idColumn = cursor.getColumnIndex(NotesContract.GoalsContentEntry._ID);
        int titleColumn = cursor.getColumnIndex(NotesContract.GoalsContentEntry.GOALS_TITLE);
        int descColumn = cursor.getColumnIndex(NotesContract.GoalsContentEntry.GOALS_DESCRIPTION);
        int dateColumn = cursor.getColumnIndex(NotesContract.GoalsContentEntry.GOALS_Date);

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
        Status.setTextColor(Color.GREEN);

        mark.setVisibility(View.GONE);
        Id.setVisibility(View.GONE);

    }


}
