package com.example.dailynotes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import com.example.dailynotes.data.NotesContract.NotesContentEntry;
import com.example.dailynotes.data.NotesContract.GoalsContentEntry;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesProvider extends ContentProvider {
    private DailyNotesDbHelper dbHelper = new DailyNotesDbHelper(getContext());
    private  static final int Notes = 100;
    private static final int Notes_Id = 101;
    private static final int Goals = 102;
    private static final int Goals_Id = 103;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    final static String LOG_TAG = NotesProvider.class.getSimpleName();

    static{
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY,NotesContract.NotesContentEntry.PATH_NOTES,Notes);
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY,NotesContract.NotesContentEntry.PATH_NOTES+"/#",Notes_Id);
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY,GoalsContentEntry.PATH_GOALS,Goals);
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY,GoalsContentEntry.PATH_GOALS+"/#",Goals_Id);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DailyNotesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match  = sUriMatcher.match(uri);
        switch (match){
            case Notes:
                cursor =database.query(NotesContentEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case Notes_Id:
                selection =NotesContentEntry._ID+ "=?" ;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(NotesContentEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case Goals:
                cursor = database.query(GoalsContentEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case Goals_Id:
                selection = GoalsContentEntry._ID+ "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(GoalsContentEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query Unkown URI"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case Notes:
                return insertNotes(uri,contentValues);
            case Goals:
                return insertGoal(uri,contentValues);
            default:
                throw new IllegalArgumentException("Cannot query Unkown URI"+uri);
        }
    }
//------------------------------------FOR INSERTING NOTE INTO DATABASE------------------------------
    public Uri insertNotes(Uri uri,ContentValues values){
        String Title = values.getAsString(NotesContentEntry.COLUMN_TITLE);
        String NotesContent = values.getAsString(NotesContentEntry.COLUMN_NOTE_TEXT);
        if(Title == null && NotesContent == null ){
            throw new IllegalArgumentException("Title and content both cannot be null");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(NotesContentEntry.TABLE_NAME,null,values);
        if(id==-1){
            Log.e("Error", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);

    }
//----------------------------------FOR INSERTING GOAL INTO DATABASE--------------------------------
    public Uri insertGoal(Uri uri,ContentValues values){
        String Title = values.getAsString(GoalsContentEntry.GOALS_TITLE);
        String Desc = values.getAsString(GoalsContentEntry.GOALS_DESCRIPTION);
        if(Title == null && Desc == null){
            throw new IllegalArgumentException("Goal and it's description cannot be null");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(GoalsContentEntry.TABLE_NAME,null,values);
        if (id==-1){
            Log.e("Error", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);

    }
//--------------------------------------------------------------------------------------------------
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case Notes:
                return database.delete(NotesContentEntry.TABLE_NAME,selection,selectionArgs);
            case Notes_Id:
                selection = NotesContentEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(NotesContentEntry.TABLE_NAME,selection,selectionArgs);
            case Goals:
                return database.delete(GoalsContentEntry.TABLE_NAME,selection,selectionArgs);
            case Goals_Id:
                selection = GoalsContentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(GoalsContentEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }
//--------------------------------------------------------------------------------------------------

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case Notes:
                return updateNote(uri, contentValues, selection, selectionArgs);
            case Notes_Id:
                selection = NotesContentEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, contentValues, selection, selectionArgs);
            case Goals:
                return updateGoal(uri,contentValues,selection,selectionArgs);
            case Goals_Id:
                selection = GoalsContentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateGoal(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
//--------------------------------------- THIS WILL UPDATE NOTES -----------------------------------
    private int updateNote(Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs){

        String Title = contentValues.getAsString(NotesContentEntry.COLUMN_TITLE);
        String NotesContent = contentValues.getAsString(NotesContentEntry.COLUMN_NOTE_TEXT);
        if(Title == null && NotesContent == null ){
            throw new IllegalArgumentException("Title and content both cannot be null");
        }

        if(contentValues.size()==0){
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri,null);
        return database.update(NotesContentEntry.TABLE_NAME,contentValues,selection,selectionArgs);
    }
//-------------------------------------- THIS WILL UPDATE GOALS ------------------------------------
    private int updateGoal(Uri uri,  ContentValues contentValues, String selection, String[] selectionArgs){
//        String title = contentValues.getAsString(GoalsContentEntry.GOALS_TITLE);
//        String desc = contentValues.getAsString(GoalsContentEntry.GOALS_DESCRIPTION);
//        if(title==null && desc==null){
//            throw new IllegalArgumentException("Goal and description both cannot be null");
//        }
        if(contentValues.size()==0){
            return 0;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri,null);
        return database.update(GoalsContentEntry.TABLE_NAME,contentValues,selection,selectionArgs);
    }
//--------------------------------------------------------------------------------------------------
}
