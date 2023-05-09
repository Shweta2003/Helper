package com.example.helper;

import android.os.Build;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalenderUtils {
    public static LocalDate selectedDate;

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date.format(formatter);
        }
        return null;
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return time.format(formatter);
        }
        return null;
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date.format(formatter);
        }
        return null;
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            yearMonth = YearMonth.from(date);
        }

        int daysInMonth = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            daysInMonth = yearMonth.lengthOfMonth();
        }

        LocalDate firstOfMonth = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            firstOfMonth = CalenderUtils.selectedDate.withDayOfMonth(1);
        }
        int dayOfWeek = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        }

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            endDate = current.plusWeeks(1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            while (current.isBefore(endDate))
            {
                days.add(current);
                current = current.plusDays(1);
            }
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            oneWeekAgo = current.minusWeeks(1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            while (current.isAfter(oneWeekAgo))
            {
                if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                    return current;

                current = current.minusDays(1);
            }
        }

        return null;
    }

}
