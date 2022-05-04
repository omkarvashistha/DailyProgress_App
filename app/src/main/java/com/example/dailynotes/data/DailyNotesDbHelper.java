package com.example.dailynotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.dailynotes.data.NotesContract.NotesContentEntry;
import com.example.dailynotes.data.NotesContract.GoalsContentEntry;

import androidx.annotation.Nullable;

public class DailyNotesDbHelper extends SQLiteOpenHelper {


    public static final String Database_Name = NotesContract.DATABASE_NAME;
    private static final int Database_Version = 1;
    Context context;

    public DailyNotesDbHelper(@Nullable Context context) {
        super(context, Database_Name, null, Database_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_NOTES_TABLE = "Create TABLE "+NotesContentEntry.TABLE_NAME+"("
                +NotesContentEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NotesContentEntry.COLUMN_TITLE+" TEXT,"
                +NotesContentEntry.COLUMN_NOTE_TEXT+" TEXT,"
                +NotesContentEntry.COLUMN_Date+" TEXT,"
                +NotesContentEntry.COLUMN_Time+" TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);

        String SQL_CREATE_GOALS_TABLE = "Create TABLE "+ NotesContract.GoalsContentEntry.TABLE_NAME + "("
                +GoalsContentEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +GoalsContentEntry.GOALS_TITLE+" TEXT,"
                +GoalsContentEntry.GOALS_DESCRIPTION+" TEXT,"
                +GoalsContentEntry.GOALS_STATUS+" TEXT,"
                +GoalsContentEntry.GOALS_Date+" TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_GOALS_TABLE);
        Toast.makeText(context, "Goals table inserted", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
