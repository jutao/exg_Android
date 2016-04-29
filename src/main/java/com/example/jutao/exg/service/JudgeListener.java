package com.example.jutao.exg.service;

import com.example.jutao.exg.bean.User;

/**
 * 监听账号判断状态接口
 */
public interface JudgeListener {
  public void getJudge(boolean result);
  public  void Failed();
}
