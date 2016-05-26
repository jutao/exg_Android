package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.util.UpdateUser;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.loopj.android.image.MySmartImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ycl.chooseavatar.library.OnChoosePictureListener;
import com.ycl.chooseavatar.library.UpLoadHeadImageDialog;
import com.ycl.chooseavatar.library.YCLTools;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends Activity implements OnChoosePictureListener {

  private static final int IMAGESUCCESS = 1;
  private static final int NETSTATUSNORMAL = 4;
  private static final int NETSTATUSFAIL = 2;
  private static final int UPDATEFAILL = 3;

  Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      String text = "";
      switch (msg.what) {
        case IMAGESUCCESS:
          msivAuth_image.setImageUrl(MyApplication.getUserInstance().getQualificat());
          msivAuth_image.setVisibility(View.VISIBLE);
          break;
        case NETSTATUSNORMAL:
          text = "认证成功";
          break;
        case NETSTATUSFAIL:
          text = "网络异常";
          break;
        case UPDATEFAILL:
          text = "更新失败";
          break;
      }
      if (Config.StringNoEmpty(text)) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
      }
    }
  };

  private TextView tvTitle;
  private Spinner spUsertype;
  private ArrayList<String> calus;
  private Button btnChoose_auth;
  private Button btnAuth_commit;
  private MySmartImageView msivAuth_image;
  private EditText edReal_name;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_auth);
    YCLTools.getInstance().setOnChoosePictureListener(this);
    initView();
    initData();
  }

  private void initView() {
    edReal_name = (EditText) findViewById(R.id.ed_real_name);
    msivAuth_image = (MySmartImageView) findViewById(R.id.msiv_auth_image);
    btnAuth_commit = (Button) findViewById(R.id.btn_auth_commit);
    btnChoose_auth = (Button) findViewById(R.id.btn_choose_auth);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    spUsertype = (Spinner) findViewById(R.id.sp_usertype);
  }

  private void initData() {
    tvTitle.setText("认证资格");
    calus = new ArrayList<String>();
    calus.add("锁匠");
    calus.add("电工");
    calus.add("管道工");
    ArrayAdapter<String> arr_adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calus);
    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spUsertype.setAdapter(arr_adapter);

    btnChoose_auth.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new UpLoadHeadImageDialog(AuthActivity.this).show();
      }
    });
    btnAuth_commit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        String name=edReal_name.getText().toString().trim();
        if(Config.StringNoEmpty(name)){
          if(Config.StringNoEmpty(MyApplication.getUserInstance().getQualificat())){
            MyApplication.getUserInstance().setCategory("1");
            MyApplication.getUserInstance()
                .setUsertype(String.valueOf((spUsertype.getSelectedItemPosition() + 1) * 10));
            MyApplication.getUserInstance().setUsertypeName(spUsertype.getSelectedItem().toString());
            MyApplication.getUserInstance().setCategoryName("是");
            MyApplication.getUserInstance().setName(name);
            userUpdate();
          }else {
            Toast.makeText(AuthActivity.this,"请选择一张证件照上传",Toast.LENGTH_LONG);
          }

        }else{
          Toast.makeText(AuthActivity.this,"请输入真实姓名",Toast.LENGTH_LONG);
        }


      }
    });
  }

  @Override public void OnChoose(String filePath) {
    getToken(filePath);
  }

  public void getToken(final String filepath) {
    new Thread() {
      @Override public void run() {
        //前一个参数是从七牛网站上得到的AccessKey,后一个参数是SecretKey
        Auth auth = Auth.create(Config.AccessKey, Config.SecretKey);
        //第一个参数是你建立的空间名称，第三个是时间长度(按毫秒算的，我这里写的是1天)
        String token = auth.uploadToken("jutaoexg", null, 1000 * 3600 * 24, null);
        if (Config.StringNoEmpty(token)) {
          UploadManager uploadManager = new UploadManager();
          File data = new File(filepath);
          //如果用“.”作为分隔的话,必须是如下写法
          String[] fileend = filepath.split("\\.");
          String key = UUID.randomUUID().toString();
          if (fileend.length > 0) {
            key += "." + fileend[fileend.length - 1];
          }
          uploadManager.put(data, key, token, new UpCompletionHandler() {
            @Override public void complete(String key, ResponseInfo info, JSONObject res) {
              try {
                String imageUrl = Config.ZONE + res.getString("key").toString();
                MyApplication.getUserInstance().setQualificat(imageUrl);
                Message msg = Message.obtain();
                msg.what = IMAGESUCCESS;
                handler.sendMessage(msg);
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          }, null);
        }
      }
    }.start();
  }

  public void userUpdate() {
    String jsonString = JSON.toJSONString(MyApplication.user);
    PrefUtils.setString(AuthActivity.this, "userInfo", jsonString);
    new UpdateUser(AuthActivity.this, new JudgeListener() {
      @Override public void getJudge(boolean result) {
        Message message = Message.obtain();

        if (result) {
          message.what = NETSTATUSNORMAL;
          finish();
        } else {
          message.what = UPDATEFAILL;
        }
        handler.sendMessage(message);
      }

      @Override public void Failed() {
        Message message = Message.obtain();
        message.what = NETSTATUSFAIL;
        handler.sendMessage(message);
      }
    });
  }

  @Override public void OnCancel() {

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    YCLTools.getInstance().upLoadImage(requestCode, resultCode, data);
  }
}
