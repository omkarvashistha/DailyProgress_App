package com.example.dailynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailynotes.data.NotesContract.GoalsContentEntry;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AddGoals extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    ImageButton backBtn,saveBtn,deleteBtn;
    TextView titleText;
    EditText title,desc;
    private Uri mCurrentUri;
    private final int NOTES_LOADER = 8496;
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
                    Toast.makeText(AddGoals.this, "Maximum characters for title reached", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Goals.class));
        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_in_up);
    }
//------------------------------------ON CREATE STARTS----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goals);
        backBtn = findViewById(R.id.back_btn_add_goals);
        saveBtn = findViewById(R.id.save_btn_add_goals);
        deleteBtn = findViewById(R.id.delete_btn_add_goals);
        title = findViewById(R.id.add_goal_title);
        desc = findViewById(R.id.add_goal_description);
        titleText = findViewById(R.id.Goals_title_bar);
//----------------------------- TO GO BACK TO GOALS ------------------------------------------------
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Goals.class));
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_down);
            }
        });
//--------------------------------------------------------------------------------------------------
        Intent intent1 = getIntent();
        mCurrentUri = intent1.getData();
        //Log.d("test", mCurrentUri.toString());
        if(mCurrentUri == null){
            titleText.setText("Add Goals");
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        else{
            titleText.setText("Your Goals");
            LoaderManager.getInstance(this).initLoader(NOTES_LOADER,null,this); //LOADER MANAGER
        }

//------This is to check if user has written something or not if yes then make save btn visible-----

        title.addTextChangedListener(mTextEditWatcher);
        desc.addTextChangedListener(mTtitleEditWatcher);
//----------------------------- TO ADD GOAL --------------------------------------------------------
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveBtn.getVisibility() == View.VISIBLE){
                    try{
                        saveGoal();
                    }
                    catch (Exception e){
                        Toast.makeText(AddGoals.this, "Cannot Add", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

//----------------------------- FUNCTION FOR SAVING GOALS ------------------------------------------
    private void saveGoal(){

//--------------------------LOGIC FOR GETTING CURRENT DATE------------------------------------------
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        Date resultdate = new Date(yourmilliseconds);

        String currDate = (sdf.format(resultdate));
        Log.d("test", currDate);

        String status = "InProgress";
        String GoalTitle = title.getText().toString();
        String GoalDesc = desc.getText().toString();

        ContentValues values = new ContentValues();
        values.put(GoalsContentEntry.GOALS_TITLE,GoalTitle);
        values.put(GoalsContentEntry.GOALS_DESCRIPTION,GoalDesc);
        values.put(GoalsContentEntry.GOALS_Date,currDate);
        values.put(GoalsContentEntry.GOALS_STATUS,status);


//---------------------WHEN WE CLICK ON + BUTTON INSERT SHOULD HAPPEN-------------------------------
        if(mCurrentUri==null){
            try{

                Uri uri = getContentResolver().insert(GoalsContentEntry.CONTENT_URI,values);
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


//------------------------------ ON CREATE ENDS HERE -----------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("test", "onCreateLoader: ");
        String[] projection ={
                GoalsContentEntry._ID,
                GoalsContentEntry.GOALS_TITLE,
                GoalsContentEntry.GOALS_DESCRIPTION,
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
            int titletxt = cursor.getColumnIndex(GoalsContentEntry.GOALS_TITLE);
            int content = cursor.getColumnIndex(GoalsContentEntry.GOALS_DESCRIPTION);

            String titleTxt = cursor.getString(titletxt);
            String contenTxt = cursor.getString(content);

            Log.d("test", "onLoadFinished: ");

            title.setText(titleTxt);
            desc.setText(contenTxt);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        title.setText("");
        desc.setText("");
        Log.d("test", "onLoaderReset: ");
    }


//------------------------------------ MAIN CLASS ENDS HERE-----------------------------------------
}