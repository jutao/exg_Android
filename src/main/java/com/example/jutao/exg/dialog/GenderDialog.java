package com.example.jutao.exg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;
import com.alibaba.fastjson.JSON;
import com.example.jutao.exg.R;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.util.UpdateUser;
import com.example.jutao.exg.volleydemo.MyApplication;

public class GenderDialog extends Dialog implements RadioGroup.OnCheckedChangeListener{
  RadioGroup rgGender;
  Activity activity;
  Fragment fragment;

  public GenderDialog(Context context, Fragment fragment) {
    super(context, R.style.AlertDialogStyle);
    activity=(Activity) context;
    this.fragment=fragment;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gender);
    rgGender=(RadioGroup)findViewById(R.id.rg_gender);
    rgGender.setOnCheckedChangeListener(this);
  }

  @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
    switch (checkedId){
      case R.id.rb_man:
        MyApplication.getUserInstance().setGender("1");
        MyApplication.getUserInstance().setGenderName("男");
        break;
      case R.id.rb_woman:
        MyApplication.getUserInstance().setGender("2");
        MyApplication.getUserInstance().setGenderName("女");
        break;
    }
    userUpdate();
  }
  public void userUpdate(){
    String jsonString = JSON.toJSONString(MyApplication.user);
    PrefUtils.setString(activity, "userInfo", jsonString);
    new UpdateUser(activity, new JudgeListener() {
      @Override public void getJudge(boolean result) {

        GenderDialog.this.dismiss();
        fragment.onResume();
      }

      @Override public void Failed() {
        GenderDialog.this.dismiss();
      }
    });
  }
}
