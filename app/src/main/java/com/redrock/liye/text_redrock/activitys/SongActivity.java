package com.redrock.liye.text_redrock.activitys;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.redrock.liye.text_redrock.R;
import com.redrock.liye.text_redrock.datas.FileDownloadThread;
import com.redrock.liye.text_redrock.datas.UserDatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by a on 2016/5/15.
 */
public class SongActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SongActivity.class.getSimpleName();
    TextView big_song;
    TextView big_singer;
    ImageView big_pic;
    ImageView big_download;
    ImageView big_play;
    ImageView big_stop;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    /** 显示下载进度TextView */
    private TextView mMessageView;
    /** 显示下载进度ProgressBar */
    private ProgressBar mProgressbar;
    String song;
    String download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String song = intent.getStringExtra("song");
        String singer = intent.getStringExtra("singer");
        String pic = intent.getStringExtra("pic");
        String download = intent.getStringExtra("download");
        this.download = download;
        this.song = song;
        String play = intent.getStringExtra("play");
        UserDatabaseHelper dbHelper =  new UserDatabaseHelper(this,"List.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("song",song);
        values.put("singer",singer);
        values.put("pic_song",pic);
        values.put("download", download);
        values.put("play", play);
        db.insert("List", null, values);
        values.clear();

        big_song = (TextView) findViewById(R.id.big_song);
        big_singer = (TextView) findViewById(R.id.big_singer);
        big_pic = (ImageView) findViewById(R.id.big_image);
        big_download = (ImageView) findViewById(R.id.big_download);
        big_play = (ImageView) findViewById(R.id.big_play);
        big_stop = (ImageView) findViewById(R.id.big_stop);
        Glide.with(this).load(pic).into(big_pic);
        big_singer.setText(singer);
        big_song.setText(song);
        big_download.setOnClickListener(this);
        big_play.setOnClickListener(this);
        big_stop.setOnClickListener(this);
        mMessageView = (TextView) findViewById(R.id.download_message);
        mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.big_download) {
            int i = initMediaPlayer();
            if (i == 0){
                mediaPlayer.reset();
                doDownload();
            }else {
                Toast.makeText(this,"你以前下过",Toast.LENGTH_SHORT).show();
            }

        }else if (v.getId() == R.id.big_play){
            int i = initMediaPlayer();
            if (i==0){
                Toast.makeText(this,"这个…………等下载完把，写代码的太弱了",Toast.LENGTH_SHORT).show();
                mediaPlayer.reset();
            }else {
                mediaPlayer.start();
            }
        }else if (v.getId() == R.id.big_stop){
            mediaPlayer.reset();

        }
    }
    private int initMediaPlayer(){
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/amosdownload/",song+".mp3");
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mProgressbar.setProgress(msg.getData().getInt("size"));

            float temp = (float) mProgressbar.getProgress()
                    / (float) mProgressbar.getMax();

            int progress = (int) (temp * 100);
            if (progress == 100) {
                Toast.makeText(SongActivity.this, "下载完成！", Toast.LENGTH_LONG).show();
            }
            mMessageView.setText("下载进度:" + progress + " %");

        }
    };

    private void doDownload() {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory()
                + "/amosdownload/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 设置progressBar初始化
        mProgressbar.setProgress(0);

        String downloadUrl = download;
        String fileName = song+".mp3";
        int threadNum = 5;
        String filepath = path + fileName;
        Log.d(TAG, "download file  path:" + filepath);
        downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
        task.start();
    }
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }

        @Override
        public void run() {

            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                Log.d(TAG, "download file http path:" + downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                mProgressbar.setMax(fileSize);

                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                        : fileSize / threadNum + 1;

                Log.d(TAG, "fileSize:" + fileSize + "  blockSize:"+blockSize);

                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize,
                            (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", downloadedAllSize);
                    mHandler.sendMessage(msg);
                    // Log.d(TAG, "current downloadSize:" + downloadedAllSize);
                    Thread.sleep(1000);// 休息1秒后再读取下载进度
                }
                Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}