package com.example.jutao.exg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.jutao.exg.service.SMSListener;
import com.example.jutao.exg.util.CreateUser;
import com.example.jutao.exg.util.SMSUtil;
import java.util.Timer;
import java.util.TimerTask;

public class RegActivity extends Activity {
  private EditText edRegAccept;
  private EditText edRegNumber;
  private EditText edRegPassword;
  private EditText edRegPasswordtwo;
  private Button btnRegAccept;
  private Button btnGetReg;
  private MyListener myListener;
  private Context context;

  private final int CLICKABLE = 0;
  private final int DISCLICKABLE = 1;

  SMSUtil smsUtil;

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case CLICKABLE:
          btnGetReg.setClickable(true);
          break;
        case DISCLICKABLE:
          btnGetReg.setClickable(false);
          break;
      }
      String btn_text = msg.getData().getString("btn_text");
      btnGetReg.setText(btn_text);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reg);
    context = this;
    smsUtil = new SMSUtil(context);
    initView();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    smsUtil.unregisterEventHandler();
  }

  private void initView() {
    edRegAccept = (EditText) findViewById(R.id.ed_reg_accept);
    edRegNumber = (EditText) findViewById(R.id.ed_reg_number);
    edRegPassword = (EditText) findViewById(R.id.ed_reg_password);
    edRegPasswordtwo = (EditText) findViewById(R.id.ed_reg_password_two);

    btnRegAccept = (Button) findViewById(R.id.btn_regaccept);
    btnGetReg = (Button) findViewById(R.id.btn_getreg);

    myListener = new MyListener();
    btnRegAccept.setOnClickListener(myListener);
    btnGetReg.setOnClickListener(myListener);
  }

  private class MyListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      ////用户输入的手机号
      // String username=edRegAccept.getText().toString().trim();
      //
      ////用户输入的验证码
      // String regNumber=edRegNumber.getText().toString().trim();
      //
      ////用户第一次输入的密码
      // String regPassword=edRegPassword.getText().toString().trim();
      //
      ////用户第二次输入的密码
      // String regPassword_two=edRegPasswordtwo.getText().toString().trim();

      switch (v.getId()) {
        case R.id.btn_regaccept:
          doReg();
          break;
        case R.id.btn_getreg:
          getReg();
          break;
      }
    }

    /**
     * 注册账号
     */
    private void doReg() {
      String username = edRegAccept.getText().toString().trim();
      String code = edRegNumber.getText().toString().trim();
      String password = edRegPassword.getText().toString().trim();
      String password_two = edRegPasswordtwo.getText().toString().trim();
      if (StringNoEmpty(username)
          && StringNoEmpty(code)
          && StringNoEmpty(password)
          && StringNoEmpty(password_two)
          && password.equals(password_two)) {

        //smsUtil.setSmsListener(username, code, new SMSListener() {
        //
        //  @Override public void onSucced() {
        //    //创建账号
        //  }
        //
        //  @Override public void onFailed(Throwable e) {
        //    Toast.makeText(context, "验证失败", Toast.LENGTH_LONG).show();
        //  }
        //});
        //
        CreateUser createUser=new CreateUser(context,username,password);
      } else {
        Toast.makeText(context, "输入有误", Toast.LENGTH_LONG).show();
      }
    }

    /**
     * 获取验证码
     */
    private void getReg() {
      //用户输入的手机号
      final String username = edRegAccept.getText().toString().trim();

      if (StringNoEmpty(username) && username.length() == 11) {
        smsUtil.setPhoneNumber(username);
        smsUtil.GetSMSVer();
        countButton();
      } else {
        Toast.makeText(context, "您输入的手机号有误", Toast.LENGTH_LONG).show();
      }
    }

    /**
     * 数字倒计时按钮
     */
    private void countButton() {
      final Timer timer = new Timer();
      final TimerTask timeTask = new TimerTask() {
        int count = 60;

        @Override public void run() {
          Message msg = Message.obtain();
          Bundle bundle = new Bundle();

          if (count < 0) {
            msg.what = CLICKABLE;
            bundle.putString("btn_text", "点击重新获取");
            msg.setData(bundle);
            handler.sendMessage(msg);
            timer.cancel();
          } else {
            msg.what = DISCLICKABLE;
            bundle.putString("btn_text", count + "S后可重试");
            msg.setData(bundle);
            handler.sendMessage(msg);
            count--;
          }
        }
      };
      /*schedule方法有三个参数
        第一个参数就是TimerTask类型的对象，我们实现TimerTask的run()方法就是要周期执行的一个任务；
        第二个参数有两种类型，第一种是long类型，表示多长时间后开始执行，另一种是Date类型，表示从那个时间后开始执行；
        第三个参数就是执行的周期，为long类型。*/
      timer.schedule(timeTask, 0, 1000);
      //timeTask.cancel();
    }

    /**
     * 判断字符串是否为空
     */
    public boolean StringNoEmpty(String str) {
      if (str != null && !str.equals("")) return true;
      return false;
    }
  }
}