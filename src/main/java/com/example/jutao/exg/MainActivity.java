package com.example.jutao.exg;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import com.baidu.mapapi.SDKInitializer;
import com.example.jutao.exg.fragment.ContentFragment;
import com.example.jutao.exg.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity {
  private static final String TAG_LEFT_MENU="TAG_LEFT_MENU";
  private static final String TAG_CONTENT="TAG_CONTENT";

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
    // 注意该方法要再setContentView方法之前实现
    SDKInitializer.initialize(getApplicationContext());
    setContentView(R.layout.activity_main);
    setBehindContentView(R.layout.left_menu);
    SlidingMenu slidingMenu=getSlidingMenu();
    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    slidingMenu.setBehindOffset(200);
    initFragment();
  }

  /**
   * 初始化fragment
   */
  private void initFragment(){
    FragmentManager fragmentManager= getSupportFragmentManager();
    //开启事务
    FragmentTransaction transaction=fragmentManager.beginTransaction();
    //帧布局容器的id;参1：帧布局容器的id，参2：要替换的fragment,参3:标记
    transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
    transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
    //提交事务
    transaction.commit();
  }
}
