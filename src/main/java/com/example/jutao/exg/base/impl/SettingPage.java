package com.example.jutao.exg.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.example.jutao.exg.base.BasePager;

/**
 * 我的账号
 */
public class SettingPage extends BasePager {
  public SettingPage(Activity mActivity) {
    super(mActivity);
  }

  @Override public void initData() {
    TextView view=new TextView(mActivity);
    view.setText("我的");
    view.setTextColor(Color.RED);
    view.setTextSize(22);
    view.setGravity(Gravity.CENTER);
    flContent.addView(view);

    tvTitle.setText("我的");

    btnMenu.setVisibility(View.GONE);
  }
}
