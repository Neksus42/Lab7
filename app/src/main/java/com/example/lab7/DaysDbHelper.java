package com.example.lab7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DaysDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "days.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DAYS = "days";
    public static final String COLUMN_DAYS_ID = "_id";
    public static final String COLUMN_DAYS_NAME = "name";
    public static final String COLUMN_DAYS_DATE = "date";

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASKS_ID = "_id";
    public static final String COLUMN_TASKS_DAY_ID = "day_id";
    public static final String COLUMN_TASKS_TEXT = "text";
    public static final String COLUMN_TASKS_CHECKED = "checked";

    private static final String CREATE_TABLE_DAYS = "CREATE TABLE " + TABLE_DAYS + " ("
            + COLUMN_DAYS_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_DAYS_NAME + " TEXT, "
            + COLUMN_DAYS_DATE + " TEXT"
            + ");";

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " ("
            + COLUMN_TASKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASKS_DAY_ID + " INTEGER, "
            + COLUMN_TASKS_TEXT + " TEXT, "
            + COLUMN_TASKS_CHECKED + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_TASKS_DAY_ID + ") REFERENCES " + TABLE_DAYS + "(" + COLUMN_DAYS_ID + ")"
            + ");";

    public DaysDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DAYS);
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);
        onCreate(db);
    }
}
