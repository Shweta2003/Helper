package com.example.helper;
import android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyWorkerAdapter extends RecyclerView.Adapter<MyWorkerAdapter.MyViewHolder> {

    private ArrayList<CheckWorker> ItemList;

    public MyWorkerAdapter(ArrayList<CheckWorker> ItemList){
        this.ItemList = ItemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button status_btn;

        private ImageView imageView;
        public MyViewHolder(final View view){
            super(view);
            name = view.findViewById(R.id.fullname);
            status_btn = view.findViewById(R.id.status_btn);
            imageView = view.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public MyWorkerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_worker_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWorkerAdapter.MyViewHolder holder, int position) {
        String nameA = ItemList.get(position).getName();
        holder.name.setText(nameA);
        String get_status = ItemList.get(position).getStatus();
        Log.d("status_val",get_status);
        String imgUrl = ItemList.get(position).getImage();
        if(get_status.equals("true")){
            holder.status_btn.setBackgroundColor(Color.rgb(120,189,40));
            holder.status_btn.setText(R.string.available);
            holder.status_btn.setTextColor(Color.BLACK);
        }
        else{
            holder.status_btn.setBackgroundColor(Color.rgb(244,67,54));
            holder.status_btn.setText(R.string.reserved);

        }
        Picasso.get().load(imgUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return  ItemList.size();
    }
}
