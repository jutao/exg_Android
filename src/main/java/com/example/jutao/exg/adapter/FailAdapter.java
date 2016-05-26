package com.example.jutao.exg.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 * Created by TIAN on 2016/5/5.
 */
public class FailAdapter extends PagerAdapter {
  ArrayList<ImageView> imageViews;
  public FailAdapter(ArrayList<ImageView> imageViews) {
    this.imageViews=imageViews;
  }

  @Override public int getCount() {
    return imageViews.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return object == view;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    ImageView view = imageViews.get(position);
    container.addView(view);
    return view;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

}
