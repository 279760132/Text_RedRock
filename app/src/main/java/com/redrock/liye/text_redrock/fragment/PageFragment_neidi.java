package com.redrock.liye.text_redrock.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2016/5/14.
 */
public class PageFragment_neidi extends Fragment {
    int i = 0;
    private List<String> song;
    private List<String> pic_song;
    private List<String> singer;
    private List<String> pic_song_Big;
    private List<String> download;
    private List<String> play;
    public static final int UPDATE = 1;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static RecyclerView neidiRecyclerView ;
    private Handler handler =  new  Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE :
                    Log.i("Redrock1", "hehe");
                    neidiRecyclerView= (RecyclerView) getView().findViewById(R.id.neidi_recycler_view);

                    Adapter_bangdan adapter_neidi = new Adapter_bangdan(song,singer,pic_song,pic_song_Big,download,play,PageFragment_neidi.this);
                    adapter_neidi.setOnItemClickListener(new Adapter_bangdan.OnItemClickListener() {
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
                    neidiRecyclerView.setAdapter(adapter_neidi);
                    neidiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    };
    public static PageFragment_neidi newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment_neidi pageFragment_neidi = new PageFragment_neidi();
        pageFragment_neidi.setArguments(args);
        return pageFragment_neidi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neidi, container, false);

        initDates();
        return view;
    }
    private void initDates(){
        String address = "https://route.showapi.com/213-4?showapi_appid=19013&topid=5&showapi_sign=7B581D1DA7E11346C0ABC1E4E35E4CFD";
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
        List pic_song = new ArrayList<String>();
        List singer = new ArrayList<String>();
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
    public Bitmap GetBitmapForUrl(String url_string) {
        URL url = null;
        try {
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            byte[] dataImage = bos.toByteArray();
            bos.close();
            in.close();
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}