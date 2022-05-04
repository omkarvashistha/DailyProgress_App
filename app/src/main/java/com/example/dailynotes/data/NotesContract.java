package com.example.dailynotes.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NotesContract {

    private  NotesContract(){}
//-------------------THESE ARE THE URI FOR CONTACTING WITH DATABASE---------------------------------
    public static final String CONTENT_AUTHORITY = "com.example.dailynotes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

//--------------------------------------------------------------------------------------------------
      public static final String DATABASE_NAME = "DailyNotes.db";

//------ THIS CLASS HELPS US TO CONTACT WITH NOTESCONTENT TABLE IN THE DATABASE---------------------

    public static  final class NotesContentEntry implements BaseColumns{

        public static final String PATH_NOTES = "Notes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NOTES);

        public final static String TABLE_NAME = "Notes";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "Title";
        public final static String COLUMN_NOTE_TEXT = "NotesContent";
        public final static String COLUMN_Date = "Date";
        public final static String COLUMN_Time = "Time";

    }
//---------------------------THIS IS CLASSS FOR GOALS-----------------------------------------------
    public static final class GoalsContentEntry implements BaseColumns{

        public static final String PATH_GOALS = "Goals";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_GOALS);

        public final static String TABLE_NAME = "Goals";

        public final static String _ID = BaseColumns._ID;
        public final static String GOALS_TITLE = "GoalTitle";
        public final static String GOALS_DESCRIPTION = "GoalDescription";
        public final static String GOALS_Date = "GoalDate";
        public final static String GOALS_STATUS = "GoalStatus";
    }

//---------------------------- CLASS FOR WEEKLY PROGRESS -------------------------------------------
    private static final class WeekklyProgressContentEntry implements BaseColumns{
        public static final String PATH_WEEKLY = "WeeklyProgress";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_WEEKLY);

        public final static String TABLE_NAME = "WeeklyProgress";

        public final static String _ID = BaseColumns._ID;
        public final static String POSITIVE_MARKS = "PositiveMarks";
        public final static String POSITIVE_DESC = "PositiveDesc";
        public final static String POSITIVE_DATE = "PositiveDate";
        public final static String NEGATIVE_MARKS = "NegativeMarks";
        public final static String NEGATIVE_DESC = "NegativeDesc";
        public final static String NEGATIVE_DATE = "NegativeDate";

    }
}
