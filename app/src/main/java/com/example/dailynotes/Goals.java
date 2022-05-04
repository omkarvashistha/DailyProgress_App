package com.example.dailynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dailynotes.data.GoalsAdapter;
import com.example.dailynotes.data.GoalsCursorAdapter;
import com.example.dailynotes.data.NotesContract;
import com.example.dailynotes.data.NotesContract.GoalsContentEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class Goals extends AppCompatActivity{
    BottomNavigationView navBar;
    ImageButton add_goal;
    private static final int NOTES_LOADER = 7454;
    TabLayout goaltabLayout;
    ViewPager goalViewPager;
    ListView goalsList;
    GoalsCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        add_goal = findViewById(R.id.AddGoals);
        goaltabLayout = findViewById(R.id.goalsTabLayout);
        goalViewPager = findViewById(R.id.goalsViewPager);
//------------------------------------BOTTOM NAVIGATION BAR-----------------------------------------
        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.nav_daily_goals);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent2 = new Intent(getApplicationContext(),HomeComponent.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.nav_daily_goals:
                        return true;

                    case R.id.nav_notes:
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.nav_week_progress:
                        Intent i = new Intent(getApplicationContext(),WeeklyProgress.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                }
                return false;
            }
        });
//-----------------------------Tab LAYOUT AND VIEW PAGER-------------------------------------------
        goaltabLayout.setupWithViewPager(goalViewPager);
        goaltabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final GoalsAdapter adapter = new GoalsAdapter(getSupportFragmentManager(),getApplicationContext(),goaltabLayout.getTabCount());
        goalViewPager.setAdapter(adapter);

        goalViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(goaltabLayout));

//---------------TO GO TO ADD GOALS-----------------------------------------------------------------
        add_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddGoals.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up);
            }
        });
//--------------------------------------------------------------------------------------------------

    }



}