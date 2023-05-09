package com.example.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends BaseAdapter {

    private ArrayList<CheckContact> myList;
    private Context context;
    LayoutInflater layoutInflater;

    public RecyclerViewAdapter(ArrayList<CheckContact> myList, Context context) {
        this.myList = myList;
        this.context = context;

    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = layoutInflater.inflate(R.layout.recycler_adapter_layout,null);
        }
        TextView name = view.findViewById(R.id.fullname);
        ImageView image = view.findViewById(R.id.image);
        name.setText(myList.get(i).getName());

        byte[] data = Base64.decode(myList.get(i).getImage(), Base64.DEFAULT);
        image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));

        return view;
    }

//    public RecyclerViewAdapter(List<CheckContact> items, Context context) {
//        this.items = items;
//        this.context = context;
//    }
//
//    private final List<CheckContact> items;
//    private final Context context;
//    @NonNull
//    @Override
//    public RecyclerViewAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_adapter_layout, null));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyHolder holder, int position) {
////        access single item
//        CheckContact myItems = items.get(position);
//
//        holder.fullname.setText(myItems.getName());
//        holder.number.setText(myItems.getNumber());
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//
////    hold instance value
//    static class MyHolder extends RecyclerView.ViewHolder{
//        private final TextView fullname,number;
//        public MyHolder(@NonNull View itemView){
//            super(itemView);
//
//            fullname = itemView.findViewById(R.id.fullname);
//            number = itemView.findViewById(R.id.Number);
//        }
//}
}
