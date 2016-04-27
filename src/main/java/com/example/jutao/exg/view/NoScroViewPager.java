package com.example.jutao.exg.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 去掉滚动功能的ViewPager
 */
public class NoScroViewPager extends ViewPager {

  public NoScroViewPager(Context context) {
    super(context);
  }

  public NoScroViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    //触摸控件什么也不做
    return true;
  }
}
