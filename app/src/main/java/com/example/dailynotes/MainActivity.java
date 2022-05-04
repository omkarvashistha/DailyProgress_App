package com.example.dailynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.dailynotes.data.NotesContract.NotesContentEntry;

import com.example.dailynotes.data.NotesContract;
import com.example.dailynotes.data.NotesCursorAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    ListView notesList;
    ImageButton add;
    BottomNavigationView navBar;
    NotesCursorAdapter mCursorAdapter;
    private static final int NOTES_LOADER = 7454;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.AddNotes);

        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.nav_notes);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent2 = new Intent(getApplicationContext(),HomeComponent.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.nav_notes:
                        return true;

                    case R.id.nav_daily_goals:
                        Intent i = new Intent(getApplicationContext(),Goals.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;

                    case R.id.nav_week_progress:
                        Intent intent = new Intent(getApplicationContext(),WeeklyProgress.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                }
                return false;
            }
        });

//----------This will set the empty view when there is no data in database-------------------------
        notesList = findViewById(R.id.notesList);
        View emptyView = findViewById(R.id.emptyView);
        notesList.setEmptyView(emptyView);
//--------------------------CURSOR ADAPTER INITIALIZED----------------------------------------------
        mCursorAdapter = new NotesCursorAdapter(this,null);
        notesList.setAdapter(mCursorAdapter);
//---------------------------SETTING ON CLICK LISTNER FOR LIST ITEMS--------------------------------
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(MainActivity.this,AddNote.class);
                intent1.setData(ContentUris.withAppendedId(NotesContentEntry.CONTENT_URI,l));
                startActivity(intent1);
            }
        });

//-----------------------ADD BUTTON TO ADD NEW NOTES------------------------------------------------
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNote.class);
                intent.setData(null);
                startActivity(intent);
            }
        });
//-----------------------------------LOADER MANAGER-------------------------------------------------
        LoaderManager.getInstance(this).initLoader(NOTES_LOADER,null,this);
//--------------------------------------------------------------------------------------------------
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id,  Bundle args) {
        String[] projection ={
                NotesContentEntry._ID,
                NotesContentEntry.COLUMN_TITLE,
                NotesContentEntry.COLUMN_NOTE_TEXT,
                NotesContentEntry.COLUMN_Date,
                NotesContentEntry.COLUMN_Time
        };

        return new CursorLoader(this,
                NotesContentEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}