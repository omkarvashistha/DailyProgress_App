package com.example.dailynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WeeklyProgress extends AppCompatActivity {
    BottomNavigationView navBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_progress);
        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(R.id.nav_week_progress);

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
                        Intent i = new Intent(getApplicationContext(),Goals.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.nav_notes:
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.nav_week_progress:
                        return true;
                }
                return false;
            }
        });


    }
}