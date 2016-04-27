package com.example.jutao.exg.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.example.jutao.exg.MainActivity;
import com.example.jutao.exg.R;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.base.impl.HomePage;
import com.example.jutao.exg.base.impl.SettingPage;
import com.example.jutao.exg.base.impl.SmartServicePage;
import com.example.jutao.exg.view.NoScroViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;

public class ContentFragment extends BaseFragment {
  ArrayList<BasePager> mBasePagers;
  NoScroViewPager mViewPager;
  RadioGroup mRadioGroup;

  @Override public View initView() {
    View view = View.inflate(mActivity, R.layout.fragment_content, null);
    mViewPager = (NoScroViewPager) view.findViewById(R.id.novp_content);
    mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_content);

    return view;
  }

  @Override public void initData() {
    mBasePagers = new ArrayList<BasePager>();
    mBasePagers.add(new HomePage(mActivity));
    mBasePagers.add(new SmartServicePage(mActivity));
    mBasePagers.add(new SettingPage(mActivity));
    mViewPager.setAdapter(new ContentPageAdapter());
    mViewPager.setOnPageChangeListener(new ContentPageAdapter());
    mRadioGroup.setOnCheckedChangeListener(new MyRadioGroupListener());

    //手动加载第一页数据
    mBasePagers.get(0).initData();
  }

  private void setSlideMenuEnable(boolean enable) {
    MainActivity mainActivity = (MainActivity) mActivity;
    if (enable) {
      mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    } else {
      mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }
  }

  private class MyRadioGroupListener implements RadioGroup.OnCheckedChangeListener {

    @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
      switch (checkedId) {
        case R.id.rb_home:
          mViewPager.setCurrentItem(0, false);//参数2代表是否有滑动动画,性能考虑填false
          break;
        case R.id.rb_smart:
          mViewPager.setCurrentItem(1, false);
          break;
        case R.id.rb_set:
          mViewPager.setCurrentItem(2, false);
          break;
      }
    }
  }

  private class ContentPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    @Override public int getCount() {
      return mBasePagers.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      BasePager basePager = mBasePagers.get(position);
      View view = basePager.mRootView;
      container.addView(view);
      //basePager.initData();        ViewPager默认加载下一个页面，为了节省性能，不再此处加载初始化数据的方法
      return view;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
      BasePager basePager = mBasePagers.get(position);
      basePager.initData();

      //首页和设置页侧边栏禁用
      if(position==mBasePagers.size()-1){
        setSlideMenuEnable(false);
      }else {
        setSlideMenuEnable(true);
      }
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  }
}
