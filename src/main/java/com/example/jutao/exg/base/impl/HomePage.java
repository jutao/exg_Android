package com.example.jutao.exg.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.MainActivity;
import com.example.jutao.exg.R;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.bean.Advertisements;
import com.example.jutao.exg.service.AdressListener;
import com.example.jutao.exg.service.AdvertisemtsListener;
import com.example.jutao.exg.talk.TalkActivity;
import com.example.jutao.exg.util.GetAdvertisements;
import com.example.jutao.exg.util.GetPositionBaidu;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.util.List;

/**
 * 首页
 */
public class HomePage extends BasePager {
  public HomePage(Activity mActivity) {
    super(mActivity);
  }

  ViewPager vpService;
  ImageButton ibTong;
  ImageButton ibLock;
  ImageButton ibLight;
  ImageButton ibQuestion;
  MyOnClickListener myOnClickListener;
  
  @Override public View initView() {
    View view = View.inflate(mActivity, R.layout.activity_service, null);
    tvTitle = (TextView) view.findViewById(R.id.tv_title);
    btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
    vpService = (ViewPager) view.findViewById(R.id.vp_service);
    ibTong = (ImageButton) view.findViewById(R.id.ib_tong);
    ibLock = (ImageButton) view.findViewById(R.id.ib_lock);
    ibLight = (ImageButton) view.findViewById(R.id.ib_light);
    ibQuestion = (ImageButton) view.findViewById(R.id.ib_question);

    myOnClickListener = new MyOnClickListener();
    ibQuestion.setOnClickListener(myOnClickListener);
    ibTong.setOnClickListener(myOnClickListener);
    ibLock.setOnClickListener(myOnClickListener);
    ibLight.setOnClickListener(myOnClickListener);
    new GetAdvertisements(mActivity, new AdvertisemtsListener() {
      @Override public void getAdvertisemts(List<Advertisements> list) {

      }
    });
    return view;
  }

  @Override public void initData() {
    btnMenu.setVisibility(View.GONE);
    tvTitle.setText("我要报修");
  }

  private class MyOnClickListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      switch (v.getId()) {
        case R.id.ib_question:
          Intent intent = new Intent(mActivity, TalkActivity.class);
          mActivity.startActivity(intent);
          break;
      }
    }
  }
}
