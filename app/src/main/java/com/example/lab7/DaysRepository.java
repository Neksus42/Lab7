package com.example.lab7;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DaysRepository {
    private static DaysRepository instance;
    private List<Day> days;

    private DaysRepository() {
        days = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        for (int i = 0; i < 7; i++) {

            String dateStr = dateFormat.format(cal.getTime());


            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            String dayName = getRussianDayName(dayOfWeek);


            days.add(new Day(i+1, dayName, dateStr, new ArrayList<>()));


            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
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

    public static DaysRepository getInstance() {
        if (instance == null) {
            instance = new DaysRepository();
        }
        return instance;
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
    }

    public void save() {

    }
}