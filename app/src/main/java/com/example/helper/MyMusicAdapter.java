package com.example.helper;
import android.content.Context;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyMusicAdapter extends RecyclerView.Adapter<MyMusicAdapter.MyViewHolder>{
    private Context context;

    private ArrayList<Integer> ItemList;
    private ArrayList<Integer> ImgList;

    IndividualSong i ;

    MediaPlayer mediaPlayer = new MediaPlayer();



    public MyMusicAdapter(ArrayList<Integer> ItemList,ArrayList<Integer> ImgList, Context context){

        this.context = context;
        this.ItemList = ItemList;
        this.ImgList = ImgList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private ImageView play,pause;
        public MyViewHolder(final View view){
            super(view);
            imageView = view.findViewById(R.id.imgup);
            play = view.findViewById(R.id.play);
            pause = view.findViewById(R.id.pause);
            pause.setVisibility(View.GONE);
            i = new IndividualSong();
            mediaPlayer = null;
        }
    }


    @NonNull
    @Override
    public MyMusicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_adapter,parent,false);
        return new MyMusicAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMusicAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(ImgList.get(position));
        Integer arr = ItemList.get(position);

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(context, arr);
                }
                mediaPlayer.start();
                holder.play.setVisibility(View.GONE);
                holder.pause.setVisibility(View.VISIBLE);

                if (mediaPlayer != null) {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                            holder.pause.setVisibility(View.GONE);
                            holder.play.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.release();
                mediaPlayer = null;
                holder.pause.setVisibility(View.GONE);
                holder.play.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }
}
