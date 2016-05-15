package com.redrock.liye.text_redrock.activitys;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.redrock.liye.text_redrock.R;
import com.redrock.liye.text_redrock.adapter.Adapter_find;
import com.redrock.liye.text_redrock.datas.UserDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2016/5/15.
 */
public class ListActivity extends AppCompatActivity {
    public static RecyclerView findRecyclerView ;
    List song = new ArrayList<String>();
    List pic_song = new ArrayList<String>();
    List singer = new ArrayList<String>();
    List pic_song_Big = new ArrayList<String>();
    List download = new ArrayList<String>();
    List play = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().hide();
        UserDatabaseHelper databaseHelper = new UserDatabaseHelper(this,"List.db",null,1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("List",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            List song1 = new ArrayList<String>();
            List pic_song1 = new ArrayList<String>();
            List singer1 = new ArrayList<String>();
            List download1 = new ArrayList<String>();
            do {
                String song = cursor.getString(cursor.getColumnIndex("song"));
                String singer = cursor.getString(cursor.getColumnIndex("singer"));
                String pic = cursor.getString(cursor.getColumnIndex("pic_song"));
                String download = cursor.getString(cursor.getColumnIndex("download"));
                Log.i("redrock99",song);
                song1.add(song);
                singer1.add(singer);
                pic_song1.add(pic);
                download1.add(download);
            }while (cursor.moveToNext());
            this.download = download1;
            this.song = song1;
            this.singer = singer1;
            this.pic_song= pic_song1;
        }
        cursor.close();

        findRecyclerView= (RecyclerView) findViewById(R.id.list_recycler_view);

        Adapter_find adapter_find = new Adapter_find(song,singer,pic_song,pic_song_Big,download,play,this);
        findRecyclerView.setAdapter(adapter_find);
        findRecyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));

    }
}
