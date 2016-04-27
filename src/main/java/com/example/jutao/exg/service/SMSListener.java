package com.example.jutao.exg.service;

/**
 * 提交短信验证码回调接口
 */
public interface SMSListener {
  void onSucced();
  void onFailed(Throwable e);
}
