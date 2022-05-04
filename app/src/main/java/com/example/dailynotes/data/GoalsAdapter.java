package com.example.dailynotes.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dailynotes.CompletedGoals;
import com.example.dailynotes.InProgressGoals;

public class GoalsAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;
    private String[] tabTitles = new String[]{"InProgress","Completed"};
    public GoalsAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                InProgressGoals inProgressGoals = new InProgressGoals();
                return inProgressGoals;
            case 1:
                CompletedGoals completedGoals = new CompletedGoals();
                return completedGoals;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
