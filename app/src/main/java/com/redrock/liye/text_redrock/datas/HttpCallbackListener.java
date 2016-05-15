package com.redrock.liye.text_redrock.datas;

/**
 * Created by a on 2016/5/14.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
