package com.example.jutao.exg.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.R;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.util.GetPositionBaidu;
import com.example.jutao.exg.volleydemo.MyApplication;

/**
 * 首页
 */
public class HomePage extends BasePager {
  Handler handler=new Handler(){
    @Override public void handleMessage(Message msg) {
      Bundle bundle=msg.getData();
      String adress=bundle.getString("adress");
      Toast.makeText(mActivity,adress,Toast.LENGTH_SHORT).show();
    }
  };
  public HomePage(Activity mActivity) {
    super(mActivity);
  }

  //@Override public View initView() {
  //  Log.d("Tag","errrrr");
  //  View view = View.inflate(mActivity, R.layout.activity_login, null);
  //  return  view;
  //}

  @Override public void initData() {
    TextView view=new TextView(mActivity);
    view.setText("我要报修");
    view.setTextColor(Color.RED);
    view.setTextSize(22);
    view.setGravity(Gravity.CENTER);
    flContent.addView(view);

    tvTitle.setText("我要报修");


    GetPositionBaidu getPositionBaidu=new GetPositionBaidu(mActivity);
    getPositionBaidu.initMap();
    setAdress();
  }
  private void  setAdress(){
    new Thread(new Runnable() {
      @Override public void run() {
        while (MyApplication.getAdress()==null){
        }
        Log.d("TAG",MyApplication.getAdress());
        Message message=Message.obtain();
        Bundle bundle=new Bundle();
        bundle.putString("adress",MyApplication.getAdress());
        message.setData(bundle);
        handler.sendMessage(message);
      }

    }).start();
  }
}
