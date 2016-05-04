package com.example.jutao.exg.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.jutao.exg.MainActivity;
import com.example.jutao.exg.R;

/**
 * 标签页的基类
 */
public class BasePager {
  public Activity mActivity;
  public TextView tvTitle;
  public ImageButton btnMenu;
  public FrameLayout flContent;//空的帧布局，要动态添加布局
  //当前页面的布局文件对象
  public View mRootView;

  public BasePager(Activity mActivity) {
    this.mActivity = mActivity;
    mRootView = initView();
  }

  public View initView() {
    View view = View.inflate(mActivity, R.layout.basepager, null);
    tvTitle = (TextView) view.findViewById(R.id.tv_title);
    btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
    flContent = (FrameLayout) view.findViewById(R.id.fl_content);
    btnMenu.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        MainActivity mainActivity= (MainActivity) mActivity;
        mainActivity.getSlidingMenu().showMenu();
      }
    });
    return view;
  }

  public void initData() {

  }
}
