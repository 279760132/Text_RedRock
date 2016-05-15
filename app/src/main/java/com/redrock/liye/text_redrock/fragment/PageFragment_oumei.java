package com.redrock.liye.text_redrock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.redrock.liye.text_redrock.R;
import com.redrock.liye.text_redrock.activitys.SongActivity;
import com.redrock.liye.text_redrock.adapter.Adapter_bangdan;
import com.redrock.liye.text_redrock.datas.HttpCallbackListener;
import com.redrock.liye.text_redrock.datas.HttpUtil;
import com.redrock.liye.text_redrock.model.Bangdanes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2016/5/14.
 */
public class PageFragment_oumei extends Fragment {
    int i = 0;
    private List<String> song;
    private List<String> pic_song;
    private List<String> singer;
    private List<String> pic_song_Big;
    private List<String> download;
    private List<String> play;
    public static final int UPDATE = 1;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static RecyclerView oumeiRecyclerView ;
    private Handler handler =  new  Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE :

                    Log.i("Redrock1", "hehe");
                    oumeiRecyclerView= (RecyclerView) getView().findViewById(R.id.oumei_recycler_view);

                    Adapter_bangdan adapter_oumei = new Adapter_bangdan(song,singer,pic_song,pic_song_Big,download,play,PageFragment_oumei.this);
                    adapter_oumei.setOnItemClickListener(new Adapter_bangdan.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view,
                                                String song,
                                                String singer,
                                                String big_pic,
                                                String play,
                                                String download) {
                            Intent intent = new Intent(getActivity(), SongActivity.class);
                            intent.putExtra("song",song);
                            intent.putExtra("singer",singer);
                            intent.putExtra("pic",big_pic);
                            intent.putExtra("play",play);
                            intent.putExtra("download",download);
                            startActivity(intent);
                        }
                    });
                    oumeiRecyclerView.setAdapter(adapter_oumei);
                    oumeiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    };
    public static PageFragment_oumei newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment_oumei pageFragment_oumei = new PageFragment_oumei();
        pageFragment_oumei.setArguments(args);
        return pageFragment_oumei;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oumei, container, false);

        initDates();
        return view;
    }
    private void initDates(){
        String address = "https://route.showapi.com/213-4?showapi_appid=19013&topid=3&showapi_sign=063C7D090EA5981F27054E60A916CE3C";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("redrock3", response);
                parseJSONWithGSON(response);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
    private void parseJSONWithGSON(String jsonData){
        List song = new ArrayList<String>();
        List singer = new ArrayList<String>();
        List pic_song = new ArrayList<String>();
        List pic_song_Big = new ArrayList<String>();
        List download = new ArrayList<String>();
        List play = new ArrayList<String>();
        Gson gson = new Gson();
        Log.i("Redrock", "1");
        Bangdanes bangdanes = gson.fromJson(jsonData, Bangdanes.class);
        Log.i("Redrock", "2");
        List<Bangdanes.ShowapiResBodyBean.PagebeanBean.SonglistBean> songlist = bangdanes.getShowapi_res_body().getPagebean().getSonglist();
        for (Bangdanes.ShowapiResBodyBean.PagebeanBean.SonglistBean c : songlist){
            pic_song_Big.add(c.getAlbumpic_big());
            download.add(c.getDownUrl());
            play.add(c.getUrl());
            singer.add(c.getSingername());
            song.add(c.getSongname());
            pic_song.add(c.getAlbumpic_small());
        }
        this.download = download;
        this.play = play;
        this.pic_song_Big = pic_song_Big;
        this.song = song;
        this.singer = singer;
        this.pic_song= pic_song;
        Message message = new Message();
        message.what = UPDATE;
        handler.sendMessage(message);
    }
}