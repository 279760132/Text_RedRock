package com.redrock.liye.text_redrock.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.redrock.liye.text_redrock.R;
import com.redrock.liye.text_redrock.adapter.Adapter_find;
import com.redrock.liye.text_redrock.datas.HttpCallbackListener;
import com.redrock.liye.text_redrock.datas.HttpUtil;
import com.redrock.liye.text_redrock.model.Findes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2016/5/15.
 */
public class FindActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private List<String> song;
    private List<String> pic_song;
    private List<String> singer;
    private List<String> pic_song_Big;
    private List<String> download;
    private List<String> play;
    private Activity activity;
    public static RecyclerView findRecyclerView ;
    private Handler handler =  new  Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1 :

                    Log.i("Redrock4", "hehe");
                    findRecyclerView= (RecyclerView) findViewById(R.id.find_recycler_view);

                    Adapter_find adapter_find = new Adapter_find(song,singer,pic_song,pic_song_Big,download,play,activity);
                    adapter_find.setOnItemClickListener(new Adapter_find.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view,
                                                String song,
                                                String singer,
                                                String big_pic,
                                                String play,
                                                String download) {
                            Intent intent = new Intent(FindActivity.this, SongActivity.class);
                            intent.putExtra("song",song);
                            intent.putExtra("singer",singer);
                            intent.putExtra("pic",big_pic);
                            intent.putExtra("play",play);
                            intent.putExtra("download",download);
                            startActivity(intent);
                        }
                    });
                    findRecyclerView.setAdapter(adapter_find);
                    findRecyclerView.setLayoutManager(new LinearLayoutManager(FindActivity.this));
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        getSupportActionBar().hide();
        activity = this;
        button = (Button) findViewById(R.id.find_button);
        editText = (EditText) findViewById(R.id.find_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String find = editText.getText().toString();
                initDates(find);
            }
        });

    }
    private void initDates(String find){
        String address = "https://route.showapi.com/213-1?page=1&showapi_appid=19013&showapi_sign=3a3822ea5d4d4949ac27858b14614ef4&keyword="+find;
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
        Findes findes = gson.fromJson(jsonData, Findes.class);
        Log.i("Redrock", "2");
        List<Findes.ShowapiResBodyBean.PagebeanBean.ContentlistBean> songlist = findes.getShowapi_res_body().getPagebean().getContentlist();
        for (Findes.ShowapiResBodyBean.PagebeanBean.ContentlistBean c : songlist){
            pic_song_Big.add(c.getAlbumpic_big());
            download.add(c.getDownUrl());
            play.add(c.getM4a());
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
        message.what = 1;
        handler.sendMessage(message);
    }
}

