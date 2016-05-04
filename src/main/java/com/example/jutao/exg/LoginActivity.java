package com.example.jutao.exg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.example.jutao.exg.bean.User;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.JudgeLogin;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.volleydemo.MyApplication;

public class LoginActivity extends Activity {
  private EditText edAccept;
  private EditText edPassword;
  private Button btn_login;
  private Button btn_reg;
  private MyListener myListener;
  private Context context;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    context=this;
    initView();
  }

  private void initView() {
    edAccept = (EditText) findViewById(R.id.ed_accept);
    edPassword = (EditText) findViewById(R.id.ed_password);
    btn_login = (Button) findViewById(R.id.btn_login);
    btn_reg = (Button) findViewById(R.id.btn_reg);

    myListener = new MyListener();
    btn_reg.setOnClickListener(myListener);
    btn_login.setOnClickListener(myListener);
  }

  private class MyListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      switch (v.getId()) {
        case R.id.btn_login:
          doLogin();
          break;
        case R.id.btn_reg:
          Intent intent=new Intent(getApplicationContext(),RegActivity.class);
          startActivity(intent);
          break;
      }
    }

    private void doLogin() {
        final String userName=edAccept.getText().toString().trim();
        final String passWord=edPassword.getText().toString().trim();
        if(Config.StringNoEmpty(userName)&&Config.StringNoEmpty(passWord)){
          JudgeLogin judgeLogin=new JudgeLogin(context, userName, passWord, new JudgeListener() {
            @Override public void getJudge(boolean result) {
              if(result){
                  //Toast.makeText(context,"登陆成功",Toast.LENGTH_LONG).show();
                PrefUtils.setString(context, "username", userName);
                PrefUtils.setString(context, "password", passWord);
                String userString=PrefUtils.getString(context,"userInfo",null);
                User user= JSON.parseObject(userString, User.class);
                if(user!=null){
                  MyApplication.user=user;
                }
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
              }else{
                  Toast.makeText(context,"账号或密码错误",Toast.LENGTH_LONG).show();
              }
            }

            @Override public void Failed() {
              Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
            }

          });
        }
    }
  }
}

