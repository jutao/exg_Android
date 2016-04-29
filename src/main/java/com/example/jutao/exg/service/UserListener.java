package com.example.jutao.exg.service;

import com.example.jutao.exg.bean.User;

/**
 * Created by TIAN on 2016/4/29.
 */
public interface UserListener {
  public void onSucced(User user);
  public void onFailed();
}
