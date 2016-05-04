package com.example.jutao.exg.volleydemo;

import android.app.Application;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jutao.exg.bean.User;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.JudgeLogin;

/**
 * 利用Application来建立一个全局的请求队列
 *
 * @author JUTAO
 */
public class MyApplication extends Application {
  //新建一个请求队列
  public static RequestQueue queues;
  public static User user=null;
  @Override public void onCreate() {
    super.onCreate();

    queues = Volley.newRequestQueue(getApplicationContext());
  }

  public static RequestQueue getHttpQueues() {
    return queues;
  }

  public static User getUserInstance() {
    if (user == null) {
      user = new User();
    }
    return user;
  }

  public static void setUser(User user) {
    MyApplication.user = user;
  }

}
