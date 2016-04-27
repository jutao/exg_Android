package com.example.jutao.exg.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
  Activity mActivity;

  //Fragment的创建
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();//获取当前Activity所依赖的activity
  }

  //初始化fragment布局
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = initView();
    return view;
  }

  /**
   * 当前Activity所依赖的activity的oncreate方法执行时
   */
  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    //初始化数据
    initData();
  }

  //初始化布局方法，子类必须实现
  public abstract View initView();

  //初始化数据方法，子类必须实现
  public abstract void initData();
}
