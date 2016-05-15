package com.redrock.liye.text_redrock.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.redrock.liye.text_redrock.R;

import java.util.List;

/**
 * Created by a on 2016/5/15.
 */
public class Adapter_find extends RecyclerView.Adapter<Adapter_find.FindViewHolder> {
    private List<String> song;
    private List<String> singer;
    private List<String> pic_song_txt;
    private Activity activity;
    private OnItemClickListener mListener;
    private List<String>pic_song_big;
    private List<String>download;
    private List<String>play;
    //private LayoutInflater inflater;
    public Adapter_find (List<String>song,
                            List<String>singer,
                            List<String>pic_song_txt,
                            List<String>pic_song_big,
                            List<String>download,
                            List<String>play,
                            Activity activity){
        this.pic_song_big = pic_song_big;
        this.download = download;
        this.play = play;
        this.song = song;
        this.singer  = singer;
        this.pic_song_txt = pic_song_txt;
        this.activity = activity;
        //this.inflater = LayoutInflater.from(context);//创建渲染。
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    @Override
    public FindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建条目缓存的视图。
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bangdan_item, parent, false);
        return new FindViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final FindViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.OnItemClick(v,
                                song.get(position),
                                singer.get(position),
                                pic_song_big.get(position),
                                play.get(position),
                                download.get(position));
                    }
                }
        );
        //绑定数据到条目.
        holder.singerName.setText(song.get(position));
        holder.songName.setText(singer.get(position));
        Glide.with(activity).load(pic_song_txt.get(position)).into(holder.songImage);
        //holder.songImage.setImageBitmap(pic_song.get(position));
    }

    @Override
    public int getItemCount() {
        //返回有多少个条目。
        return singer.size();
    }



    public interface OnItemClickListener {
        public void OnItemClick(View view,
                                String song,
                                String singer,
                                String big_pic,
                                String play,
                                String download);
    }
    class FindViewHolder extends RecyclerView.ViewHolder{
        TextView songName ;
        TextView singerName;
        ImageView songImage;
        public FindViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            songImage = (ImageView) itemView.findViewById(R.id.song_image);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            singerName = (TextView) itemView.findViewById(R.id.singer_name);
        }
    }


}