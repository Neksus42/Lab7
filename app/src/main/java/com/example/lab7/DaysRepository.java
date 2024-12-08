package com.example.lab7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DaysRepository {
    private static DaysRepository instance;
    private List<Day> days;

    private Context context;
    private DaysDbHelper dbHelper;

    private DaysRepository(Context context) {
        this.context = context.getApplicationContext();
        dbHelper = new DaysDbHelper(this.context);
        days = loadDaysFromDb();

        if (days.isEmpty()) {
            days = generateInitialDays();
            saveAllToDb(days);
        }
    }

    public static synchronized DaysRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DaysRepository(context);
        }
        return instance;
    }


    private List<Day> generateInitialDays() {
        List<Day> initialDays = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String dateStr = dateFormat.format(cal.getTime());
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            String dayName = getRussianDayName(dayOfWeek);
            initialDays.add(new Day(i+1, dayName, dateStr, new ArrayList<>()));
            cal.add(Calendar.DAY_OF_MONTH, 1);

        }
        return initialDays;
    }

    private String getRussianDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "Понедельник";
            case Calendar.TUESDAY:
                return "Вторник";
            case Calendar.WEDNESDAY:
                return "Среда";
            case Calendar.THURSDAY:
                return "Четверг";
            case Calendar.FRIDAY:
                return "Пятница";
            case Calendar.SATURDAY:
                return "Суббота";
            case Calendar.SUNDAY:
            default:
                return "Воскресенье";
        }
    }

    public List<Day> getDays() {
        return days;
    }

    public Day getDayById(int id) {
        for (Day d : days) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    public void removeDay(int id) {

        Iterator<Day> iterator = days.iterator();
        while (iterator.hasNext()) {
            Day d = iterator.next();
            if (d.getId() == id) {
                iterator.remove();
                break;
            }
        }


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DaysDbHelper.TABLE_TASKS, DaysDbHelper.COLUMN_TASKS_DAY_ID + "=?", new String[]{String.valueOf(id)});
        db.delete(DaysDbHelper.TABLE_DAYS, DaysDbHelper.COLUMN_DAYS_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void save() {

        saveAllToDb(days);
    }


    private List<Day> loadDaysFromDb() {
        List<Day> loadedDays = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DaysDbHelper.TABLE_DAYS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DaysDbHelper.COLUMN_DAYS_ID);
            int nameIndex = cursor.getColumnIndex(DaysDbHelper.COLUMN_DAYS_NAME);
            int dateIndex = cursor.getColumnIndex(DaysDbHelper.COLUMN_DAYS_DATE);

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String date = cursor.getString(dateIndex);

                List<TaskItem> tasks = loadTasksForDay(db, id);

                Day day = new Day(id, name, date, tasks);
                loadedDays.add(day);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return loadedDays;
    }


    private List<TaskItem> loadTasksForDay(SQLiteDatabase db, int dayId) {
        List<TaskItem> tasks = new ArrayList<>();

        Cursor taskCursor = db.query(DaysDbHelper.TABLE_TASKS,
                null,
                DaysDbHelper.COLUMN_TASKS_DAY_ID + "=?",
                new String[]{String.valueOf(dayId)},
                null, null, null);

        if (taskCursor.moveToFirst()) {
            int textIndex = taskCursor.getColumnIndex(DaysDbHelper.COLUMN_TASKS_TEXT);
            int checkedIndex = taskCursor.getColumnIndex(DaysDbHelper.COLUMN_TASKS_CHECKED);

            do {
                String text = taskCursor.getString(textIndex);
                int checkedInt = taskCursor.getInt(checkedIndex);
                boolean checked = (checkedInt == 1);
                tasks.add(new TaskItem(text, checked));
            } while (taskCursor.moveToNext());
        }
        taskCursor.close();

        return tasks;
    }


    private void saveAllToDb(List<Day> days) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {

            db.delete(DaysDbHelper.TABLE_TASKS, null, null);
            db.delete(DaysDbHelper.TABLE_DAYS, null, null);

            for (Day d : days) {
                ContentValues dayValues = new ContentValues();
                dayValues.put(DaysDbHelper.COLUMN_DAYS_ID, d.getId());
                dayValues.put(DaysDbHelper.COLUMN_DAYS_NAME, d.getName());
                dayValues.put(DaysDbHelper.COLUMN_DAYS_DATE, d.getDate());
                db.insert(DaysDbHelper.TABLE_DAYS, null, dayValues);


                for (TaskItem t : d.getTasks()) {
                    ContentValues taskValues = new ContentValues();
                    taskValues.put(DaysDbHelper.COLUMN_TASKS_DAY_ID, d.getId());
                    taskValues.put(DaysDbHelper.COLUMN_TASKS_TEXT, t.getText());
                    taskValues.put(DaysDbHelper.COLUMN_TASKS_CHECKED, t.isChecked() ? 1 : 0);
                    db.insert(DaysDbHelper.TABLE_TASKS, null, taskValues);
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
    }
}
