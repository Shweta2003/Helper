package com.example.helper;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.sql.DataTruncation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class CustomCalenderView extends LinearLayout {

    ImageButton next,prev;
    TextView currdate;
    GridView gridView;
    private static final int MAX_CALENDER_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFoemat = new SimpleDateFormat("yyyy",Locale.ENGLISH);

    SimpleDateFormat eventDateFormt = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventList = new ArrayList<>();
    int AlarmYear, AlarmMonth, AlarmDay,AlarmHour,AlarmMinute;

    public CustomCalenderView(Context context) {
        super(context);
    }

    public CustomCalenderView(Context context, @Nullable AttributeSet attre){
        super(context,attre);
        this.context = context;
        InitialLayout();
        SetUpCalender();

        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,-1);
                SetUpCalender();
            }
        });

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalender();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.add_new_event_layout,null);
                final EditText EventName = addView.findViewById(R.id.events_id);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                ImageButton settime = addView.findViewById(R.id.seteventtime);
                CheckBox  checkBox = addView.findViewById(R.id.alarm_me);
                Calendar dateCalender = Calendar.getInstance();
                dateCalender.setTime(dates.get(i));
                AlarmYear = dateCalender.get(Calendar.YEAR);
                AlarmMonth = dateCalender.get(Calendar.MONTH);
                AlarmDay = dateCalender.get(Calendar.DAY_OF_MONTH);


                Button addbutton = addView.findViewById(R.id.addevent);
                settime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar1 = Calendar.getInstance();
                        int hours = calendar1.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar1.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY,i);
                                        c.set(Calendar.MINUTE,i1);
                                        timePicker.setBackgroundColor(Color.GREEN);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformat = new SimpleDateFormat("K:mm a",Locale.ENGLISH);
                                        String event_Time = hformat.format(c.getTime());
                                        EventTime.setText(event_Time);
                                        AlarmHour = c.get(Calendar.HOUR_OF_DAY);
                                        AlarmMinute = c.get(Calendar.MINUTE);

                                    }
                                },hours,minute,false);
                        timePickerDialog.show();
                    }
                });
                final String date = eventDateFormt.format(dates.get(i));
                final String month = monthFormat.format(dates.get(i));
                final String year = yearFoemat.format(dates.get(i));
                addbutton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkBox.isChecked()){
                            SaveEvent(EventName.getText().toString(),EventTime.getText().toString(),date,month,year,"on");
                            SetUpCalender();
                            Calendar calendar  = Calendar.getInstance();
                            calendar.set(AlarmYear,AlarmMonth,AlarmDay,AlarmHour,AlarmMinute);
                            setAlarm(calendar,EventName.getText().toString(),EventTime.getText().toString(),getRequestCode(date,EventName.getText().toString(),EventTime.getText().toString()));
                            alertDialog.dismiss();
                        }else {
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year,"off");
                            SetUpCalender();
                            alertDialog.dismiss();
                        }
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        SetUpCalender();
                    }
                });
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                String dateA = eventDateFormt.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View showView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.show_event_layout,null);
                RecyclerView recyclerView = showView.findViewById(R.id.eventRev);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(),CollectEventsByDate(dateA));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                return true;
            }

        });
    }

    private int getRequestCode(String dtata, String event, String time){
        int code = 0;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIdEvents(dtata,event,time,database);
        while(cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(DBStructur.ID));

        }
        cursor.close();
        dbOpenHelper.close();

        return code;
    }

    private void setAlarm(Calendar calendar, String event, String time, int RequestCose){
        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
        intent.putExtra("event",event);
        intent.putExtra("time",time);
        intent.putExtra("id",RequestCose);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCose,intent,PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }


    private ArrayList<Events> CollectEventsByDate(String Date){
        ArrayList<Events> arrayList = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(Date,database);
        while(cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructur.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructur.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructur.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructur.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructur.YEAR));
            Events events = new Events(event,time,date,month,Year);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    public CustomCalenderView(Context context,@Nullable AttributeSet attre, int dAttre){
        super(context,attre,dAttre);

    }

    private void SaveEvent(String event, String time, String date, String month, String year, String notify){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,notify,database);
        dbOpenHelper.close();
        Toast.makeText(context,"Event Saved",Toast.LENGTH_SHORT).show();
    }

    private void InitialLayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout,this);
        prev = view.findViewById(R.id.previousbtn);
        next = view.findViewById(R.id.nextbtn);
        currdate = view.findViewById(R.id.currdate);
        gridView = view.findViewById(R.id.gridView);
    }

    private void SetUpCalender(){
        String cuurentDate = simpleDateFormat.format(calendar.getTime());
        currdate.setText(cuurentDate);
        dates.clear();
        Calendar monthCalender = (Calendar) calendar.clone();
        monthCalender.set(Calendar.DAY_OF_MONTH,1);
        int FirstDaysOfMonth = monthCalender.get(Calendar.DAY_OF_WEEK)-1;
        monthCalender.add(Calendar.DAY_OF_MONTH, - FirstDaysOfMonth);
        CollectEventPerMonth(monthFormat.format(calendar.getTime()),yearFoemat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDER_DAYS){
            dates.add(monthCalender.getTime());
            monthCalender.add(Calendar.DAY_OF_MONTH,1);
        }
        myGridAdapter = new MyGridAdapter(context,dates,calendar,eventList);
        gridView.setAdapter(myGridAdapter);
    }

    private void CollectEventPerMonth(String Month,String year){
        eventList.clear();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsMonth(Month,year,database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructur.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructur.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructur.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructur.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructur.YEAR));
            Events events = new Events(event,time,date,month,Year);
            eventList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }
}
