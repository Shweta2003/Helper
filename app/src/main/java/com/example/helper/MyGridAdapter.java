package com.example.helper;

import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentdate;
    List<Events>events;
    LayoutInflater layoutInflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentdate, List<Events> events) {
        super(context, R.layout.single_cell);
        this.dates = dates;
        this.currentdate = currentdate;
        this.events = events;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View ContentView, @NonNull ViewGroup parent){

        Date monthDate = dates.get(position);
        Calendar dateCalender = Calendar.getInstance();
        dateCalender.setTime(monthDate);
        int DaysMo = dateCalender.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalender.get(Calendar.MONTH)+1;
        int displayYear = dateCalender.get(Calendar.YEAR);
        int currentMonth = currentdate.get(Calendar.MONTH)+1;
        int currentYear = currentdate.get(Calendar.YEAR);
        View view = ContentView;
        if(view == null){
            view = layoutInflater.inflate(R.layout.single_cell,parent,false);
        }
        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(Color.argb(65,0,0,0));
        }
        else{
            view.setBackgroundColor(Color.argb(65,90,89,89));
        }
        TextView Day_number = view.findViewById(R.id.calender_day);
        Day_number.setText(String.valueOf(DaysMo));
        TextView EventNumber = view.findViewById(R.id.events_id);
        Calendar eventCalender = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i < events.size(); i ++){
            eventCalender.setTime(CurrentStringToDate(events.get(i).getDATE()));
            if(DaysMo == eventCalender.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalender.get(Calendar.MONTH)+1
            && displayYear == eventCalender.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT());
                EventNumber.setText(arrayList.size() + " events");

            }
        }
        return view;
    }

    private Date CurrentStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);

        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getCount(){
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item){
        return dates.indexOf(item);
    }

    @Override
    public Object getItem(int position){
        return dates.get(position);
    }
}
