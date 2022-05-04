package com.example.dailynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailynotes.data.DailyNotesDbHelper;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dailynotes.data.NotesContract;
import com.example.dailynotes.data.NotesCursorAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import static com.google.android.material.navigation.NavigationBarView.*;

public class HomeComponent extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private NotesCursorAdapter mCursorAdapter;
    private final int NOTES_LOADER = 7454;
    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_component);

//----------- THIS WILL SET THE NAVBAR AND WILL BE ABLE TO NAVIGATE TO DIFFRENT OPTIONS ------------
        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.nav_home);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        return true;

                    case R.id.nav_notes:
                        Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
//--------------------------------------------------------------------------------------------------
    }


}