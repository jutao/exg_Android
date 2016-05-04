package com.example.jutao.exg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.util.UpdateUser;
import com.example.jutao.exg.volleydemo.MyApplication;

public class NeckActivity extends Activity {

  TextView tvTitle;
  EditText edNeck_name;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_neck);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    tvTitle.setText("更改名字");
    edNeck_name=(EditText)findViewById(R.id.ed_neck_name);
    if(Config.StringNoEmpty(MyApplication.getUserInstance().getNickname())){
      edNeck_name.setText(MyApplication.getUserInstance().getNickname());
      edNeck_name.setSelection(MyApplication.getUserInstance().getNickname().length());
    }
  }

  /**
   * 保存按钮的click
   * @param view
   */
  public void save(View view){
      String neckName=edNeck_name.getText().toString().trim();
      String myNeckName=MyApplication.getUserInstance().getNickname();
      if(Config.StringNoEmpty(neckName)&&!neckName.equals(myNeckName)){
        MyApplication.getUserInstance().setNickname(neckName);
        userUpdate();
      }
  }
  public void userUpdate(){
    String jsonString = JSON.toJSONString(MyApplication.user);
    PrefUtils.setString(NeckActivity.this, "userInfo", jsonString);
    new UpdateUser(NeckActivity.this, new JudgeListener() {
      @Override public void getJudge(boolean result) {
        String text="";
        if (result) {
          text="设置成功";
        } else {
          text="更新失败";
        }
        Toast.makeText(NeckActivity.this,text,Toast.LENGTH_LONG).show();
        finish();
      }

      @Override public void Failed() {
        Toast.makeText(NeckActivity.this, "网络异常", Toast.LENGTH_LONG).show();
        finish();
      }
    });
  }
}
