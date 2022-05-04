package com.example.dailynotes;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dailynotes.data.GoalsCursorAdapter;
import com.example.dailynotes.data.NotesContract;

public class InProgressGoals extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView goalsList;
    GoalsCursorAdapter mCursorAdapter;
    int NOTES_LOADER = 7454;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_in_progress_goals, container, false);
        goalsList = view.findViewById(R.id.In_progress_goals_list_view);

//----------------------------- POPULATING LIST VIEW -----------------------------------------------
        View emptyView = view.findViewById(R.id.empty_view_goals);
        goalsList.setEmptyView(emptyView);   // When there is no data in database

        mCursorAdapter = new GoalsCursorAdapter(view.getContext(),null);
        goalsList.setAdapter(mCursorAdapter);

//--------------------------------ON CLICK ON LISTVIEW ITEM-----------------------------------------
        goalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getActivity(),AddGoals.class);
                intent1.setData(ContentUris.withAppendedId(NotesContract.GoalsContentEntry.CONTENT_URI,l));
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up);
            }
        });

        LoaderManager.getInstance(getActivity()).initLoader(NOTES_LOADER,null,this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String [] projection = {
                NotesContract.GoalsContentEntry._ID,
                NotesContract.GoalsContentEntry.GOALS_TITLE,
                NotesContract.GoalsContentEntry.GOALS_DESCRIPTION,
                NotesContract.GoalsContentEntry.GOALS_STATUS,
                NotesContract.GoalsContentEntry.GOALS_Date
        };
        String selection = NotesContract.GoalsContentEntry.GOALS_STATUS+" = \"InProgress\"";
        return new CursorLoader(getContext(),
                NotesContract.GoalsContentEntry.CONTENT_URI,
                projection,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull  Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}