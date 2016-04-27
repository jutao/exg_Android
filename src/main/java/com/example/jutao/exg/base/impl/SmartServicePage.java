package com.example.jutao.exg.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.example.jutao.exg.base.BasePager;

/**
 * 赚钱
 */
public class SmartServicePage extends BasePager {
  public SmartServicePage(Activity mActivity) {
    super(mActivity);
  }

  @Override public void initData() {
    TextView view=new TextView(mActivity);
    view.setText("赚钱");
    view.setTextColor(Color.RED);
    view.setTextSize(22);
    view.setGravity(Gravity.CENTER);
    flContent.addView(view);

    tvTitle.setText("赚钱");

  }
}
