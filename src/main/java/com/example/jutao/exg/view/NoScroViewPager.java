package com.example.jutao.exg.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 去掉滚动功能的ViewPager
 */
public class NoScroViewPager extends ViewPager {


  private boolean isCanScroll=false;
  public boolean isCanScroll() {
    return isCanScroll;
  }

  public void setIsCanScroll(boolean isCanScroll) {
    this.isCanScroll = isCanScroll;
  }
  public NoScroViewPager(Context context) {
    super(context);
  }

  public NoScroViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    return true;
  }

  @Override
  public void scrollTo(int x, int y){
    if (isCanScroll){
      super.scrollTo(x, y);
    }
  }
}