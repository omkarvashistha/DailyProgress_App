package com.example.dailynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//import android.icu.text.SimpleDateFormat;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailynotes.data.NotesContract;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.example.dailynotes.data.NotesContract.NotesContentEntry;

/* COMMENTS FOR XML FILE AS COMMENTING THERE CREATES SOME PROBLEMS
    --> Background="@null" makes the button transparent

 */




public class AddNote extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText notesTitle,notesText;
    TextView add_note_title;
    ImageButton backBtn,saveBtn,deleteBtn;
    private Uri mCurrentUri;
    private final int NOTES_LOADER = 8496;

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

    }
//------------THIS WILL CHECK FOR TITLE TO NOT BE MORE THAN EXPECTED ALPHABETS----------------------

    private final TextWatcher mTtitleEditWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence string, int i, int i1, int i2) {
            if(string.length()>0){
                saveBtn.setVisibility(View.VISIBLE);
                if(string.length()==40){
                    Toast.makeText(AddNote.this, "Maximum characters for title reached", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                saveBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


//------THIS WILL CHECK EVERYTIME THE TEXT IS CHANGED AND MAKE SAVE BUTTON VISIBLE OR INVISIBLE-----
    private  final TextWatcher mTextEditWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void onTextChanged(CharSequence string, int i, int i1, int i2) {
        if(string.length()>0){
            saveBtn.setVisibility(View.VISIBLE);
        }
        else{
            saveBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
};
//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        notesTitle = findViewById(R.id.notes_title);
        notesText = findViewById(R.id.notes_text);
        backBtn = findViewById(R.id.back_btn_add_notes);
        saveBtn = findViewById(R.id.save_btn_add_notes);
        deleteBtn = findViewById(R.id.delete_btn_add_notes);
        add_note_title = findViewById(R.id.Add_notes_title_bar);
//--------------------------RECIEVE INTENT FROM MAIN-ACTIVITY----------------------------------------
        Intent intent1 = getIntent();
        mCurrentUri = intent1.getData();
        //Log.d("test", mCurrentUri.toString());
        if(mCurrentUri == null){
            add_note_title.setText("Add Notes");
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        else{
            add_note_title.setText("Your Notes");
//-----------------------------------LOADER MANAGER-------------------------------------------------
            LoaderManager.getInstance(this).initLoader(NOTES_LOADER,null,this);
//--------------------------------------------------------------------------------------------------
        }

//-------------------To go back to main activity---------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNote.this,MainActivity.class);
                startActivity(intent);
            }
        });


//------This is to check if user has written something or not if yes then make save btn visible-----

        notesText.addTextChangedListener(mTextEditWatcher);
        notesTitle.addTextChangedListener(mTtitleEditWatcher);
//--------------------------------TO ADD NOTES------------------------------------------------------
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveBtn.getVisibility() == View.VISIBLE){
                    try{
                        saveNotes();
                    }
                    catch (Exception e){
                        Toast.makeText(AddNote.this, "Cannot Add", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
//----------------------------------TO DELETE NOTES-------------------------------------------------
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteBtn.getVisibility() == View.VISIBLE){
                    try{
                        deleteNotes();
                    }
                    catch (Exception e){
                        Toast.makeText(AddNote.this, "Cannot Add", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//--------------------------------------------------------------------------------------------------
    }
//--------------------------------SAVE-NOTES METHOD STARTS HERE-------------------------------------
    private void saveNotes(){
//--------------------------LOGIC FOR GETTING CURRENT DATE------------------------------------------
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        Date resultdate = new Date(yourmilliseconds);

        String currDate = (sdf.format(resultdate));
        Log.d("test", currDate);

//---------------------------LOGIC FOR GETTING CURRENT TIME-----------------------------------------
        SimpleDateFormat sdt = new SimpleDateFormat("HH:mm");
        //sdt.setTimeZone(TimeZone.getTimeZone("IST"));
        Date resultTime = new Date(yourmilliseconds);
        String time = sdt.format(resultTime);
        int t = Integer.parseInt(time.substring(0,2));
        if(t>=12 & t<24){
            t%=12;
            time = String.valueOf(t)+time.substring(2,time.length());
            time+= " pm";
        }
        else{
            time+= " am";
        }
        Log.d("test", time);
//--------------------------------------------------------------------------------------------------
        String titleStr = notesTitle.getText().toString();
        String NotesStr = notesText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(NotesContentEntry.COLUMN_TITLE,titleStr);
        values.put(NotesContentEntry.COLUMN_NOTE_TEXT,NotesStr);
        values.put(NotesContentEntry.COLUMN_Date,currDate);
        values.put(NotesContentEntry.COLUMN_Time,time);
//---------------------WHEN WE CLICK ON + BUTTON INSERT SHOULD HAPPEN-------------------------------
        if(mCurrentUri==null){
            try{

                Uri uri = getContentResolver().insert(NotesContentEntry.CONTENT_URI,values);
                if(uri==null){
                    Toast.makeText(getApplicationContext(), "Insert failed", Toast.LENGTH_SHORT).show();
                }
                else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(saveBtn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    Toast.makeText(getApplicationContext(), "Inserted..", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e){
                Toast.makeText(this, "Exception ocuured", Toast.LENGTH_LONG).show();
            }
        }
//----------------------WHEN WE TAP ON LIST VIEW AND SAVE UPDATE SHOULD HAPPEN----------------------
        else{
            try{
                int affectedRows = getContentResolver().update(mCurrentUri,values,null,null);
                if(affectedRows==0){
                    Log.d("Notes", "No rows updated");
                }
                else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(saveBtn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this, "Exception ocuured", Toast.LENGTH_LONG).show();
            }
        }
    }
//---------------------------------SAVE NOTES END HERE--------------------------------------------

//-----------------------------------DELETE NOTES START HERE----------------------------------------
    private void deleteNotes(){
        getContentResolver().delete(mCurrentUri,null,null);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("test", "onCreateLoader: ");
        String[] projection ={
                NotesContentEntry._ID,
                NotesContentEntry.COLUMN_TITLE,
                NotesContentEntry.COLUMN_NOTE_TEXT,
        };
        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if(cursor.moveToFirst()){
            int title = cursor.getColumnIndex(NotesContentEntry.COLUMN_TITLE);
            int content = cursor.getColumnIndex(NotesContentEntry.COLUMN_NOTE_TEXT);

            String titleTxt = cursor.getString(title);
            String contenTxt = cursor.getString(content);

            Log.d("test", "onLoadFinished: ");

            notesTitle.setText(titleTxt);
            notesText.setText(contenTxt);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notesTitle.setText("");
        notesText.setText("");
        Log.d("test", "onLoaderReset: ");
    }
}