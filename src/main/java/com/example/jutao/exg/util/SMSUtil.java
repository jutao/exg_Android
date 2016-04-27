package com.example.jutao.exg.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import com.example.jutao.exg.service.SMSListener;

public class SMSUtil {

  static MySMSHandler  mSMSHandle;
  static SMSListener mIValidateSMSCode;
  //短信SDK相关证件信息
  private final String APPKEY = "11f1cedb776e8";

  private final String APPSECRET = "6d6933394c76e8e8c72e8209ffd5d740";

  private String phoneNumber;
  private String code;
  private Context context;

  public SMSUtil(Context context){
    this.context=context;
    mSMSHandle=new MySMSHandler();
    initSDK(context);
  }

  private static EventHandler eh = new EventHandler() {

    @Override public void afterEvent(int event, int result, Object data) {
      Message msg = new Message();
      msg.arg1 = event;
      msg.arg2 = result;
      msg.obj = data;
      mSMSHandle.sendMessage(msg);
    }
  };

  private   void initSDK(Context context) {
    SMSSDK.initSDK(context, APPKEY, APPSECRET);
  }
  public  void GetSMSVer(){
    SMSSDK.getVerificationCode("86", phoneNumber, new OnSendMessageHandler() {
      @Override public boolean onSendMessage(String s, String s1) {
        return false;
      }
    });
  }
  private   void SMSVer(){
    Log.d("Tagg",phoneNumber+"code:"+code);
    SMSSDK.submitVerificationCode("86", phoneNumber, code);
  }

  private  void registerEventHandler(){
    SMSSDK.registerEventHandler(eh);
  }
  public  void unregisterEventHandler() {
    SMSSDK.unregisterEventHandler(eh);
  }


  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setSmsListener(String phonenumber,String code,SMSListener smsListener) {
    setPhoneNumber(phonenumber);
    setCode(code);
    mIValidateSMSCode = smsListener;
    registerEventHandler();
    SMSVer();
  }

  /**
   * 消息处理Handle
   */
  private static class MySMSHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      int event = msg.arg1;
      int result = msg.arg2;
      Object data = msg.obj;
      if (result == SMSSDK.RESULT_COMPLETE) {
        //提交验证码成功
        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
          //验证成功回调
          if(null != mIValidateSMSCode){
            mIValidateSMSCode.onSucced();
          }
        }
      } else {
        Throwable exption = ((Throwable) data);
        //验证成功回调
        if(null != mIValidateSMSCode){
          mIValidateSMSCode.onFailed(exption);
        }
      }
    }
  }


}
