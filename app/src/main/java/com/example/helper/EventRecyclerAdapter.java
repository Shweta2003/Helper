package com.example.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    Context context;
    ArrayList<Events> arrayList;
    DBOpenHelper dbOpenHelper;

    public EventRecyclerAdapter(Context context,ArrayList<Events> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_row_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Events events = arrayList.get(position);
        int p = position;
        holder.Event.setText(events.getEVENT());
        holder.Datetxt.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCalenderEvent(events.getEVENT(),events.getDATE(),events.getTIME());
                arrayList.remove(p);
                notifyDataSetChanged();
            }
        });

        if(isAlarmed(events.getDATE(),events.getEVENT(),events.getTIME())){
            holder.setAlarm.setImageResource(R.drawable.baseline_notifications_24);
        }else{
            holder.setAlarm.setImageResource(R.drawable.baseline_notifications_off_24);
        }
        Calendar datecalendar = Calendar.getInstance();
        datecalendar.setTime(CurrentStringToDate(events.getDATE()));
        int alarmYear = datecalendar.get(Calendar.YEAR);
        int alarmMonth = datecalendar.get(Calendar.MONTH);
        int alarmDay = datecalendar.get(Calendar.DAY_OF_MONTH);
        Calendar timecalendar = Calendar.getInstance();
        timecalendar.setTime(CurrentStringTotime(events.getTIME()));
        int alarmHour = datecalendar.get(Calendar.HOUR_OF_DAY);
        int alaromMinute = datecalendar.get(Calendar.MINUTE);
        holder.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAlarmed(events.getDATE(),events.getEVENT(),events.getTIME())){
                    holder.setAlarm.setImageResource(R.drawable.baseline_notifications_off_24);
                    CancelAlarm(getRequestCode(events.getDATE(),events.getEVENT(),events.getTIME()));
                    updateEvent(events.getDATE(),events.getEVENT(),events.getTIME(),"off");
                    notifyDataSetChanged();
                }else{
                    holder.setAlarm.setImageResource(R.drawable.baseline_notifications_24);
                    Calendar alarmClender = Calendar.getInstance();
                    alarmClender.set(alarmYear,alarmMonth,alarmDay,alarmHour,alaromMinute);
                    setAlarm(alarmClender,events.getEVENT(),events.getTIME(),getRequestCode(events.getDATE(),events.getEVENT(),events.getTIME()));
                    updateEvent(events.getDATE(),events.getEVENT(),events.getTIME(),"on");
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Datetxt,Event,Time;
        Button deleteButton;
        ImageButton setAlarm;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Datetxt = itemView.findViewById(R.id.eventDate);
            Event = itemView.findViewById(R.id.eventName);
            Time = itemView.findViewById(R.id.eventTime);
            deleteButton = itemView.findViewById(R.id.delete_event);
            setAlarm = itemView.findViewById(R.id.alarmBtm);
        }
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

    private Date CurrentStringTotime(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);

        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    private void deleteCalenderEvent(String name, String date, String time){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(name,date,time,database);
        dbOpenHelper.close();

    }

    private boolean isAlarmed(String date, String event, String time){
        boolean alarm = false;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIdEvents(date,event,time,database);
        while(cursor.moveToNext()){
            String notify = cursor.getString(cursor.getColumnIndex(DBStructur.Notify));
            if(notify.equals("on")){
                alarm = true;
            }
            else{
                alarm = false;
            }
        }
        cursor.close();
        dbOpenHelper.close();
        return alarm;
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

    private void CancelAlarm(int RequestCose){
        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCose,intent,PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
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

    private void updateEvent(String date, String event, String  time, String notify){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.updateEvent(date,event,time,notify,database);
        dbOpenHelper.close();
    }


}
