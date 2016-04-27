package com.example.jutao.exg.volleydemo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 利用Application来建立一个全局的请求队列
 *
 * @author JUTAO
 */
public class MyApplication extends Application {
  //新建一个请求队列
  public static RequestQueue queues;
  public static String userKey;
  public static String adress;

  @Override public void onCreate() {
    super.onCreate();
    queues = Volley.newRequestQueue(getApplicationContext());
  }

  public static RequestQueue getHttpQueues() {
    return queues;
  }

  public static String getUserKey() {
    return userKey;
  }

  public static String getAdress() {
    return adress;
  }
}
