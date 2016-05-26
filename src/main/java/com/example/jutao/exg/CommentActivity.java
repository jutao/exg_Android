package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.jutao.exg.bean.Comment;
import com.example.jutao.exg.bean.V_task;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.CreateComment;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.util.UpdateTask_Order;
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
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentActivity  extends Activity implements OnChoosePictureListener {



  private static final int IMAGESUCCESS = 1;
  Handler handler=new Handler(){
    @Override public void handleMessage(Message msg) {
      switch (msg.what){
        case IMAGESUCCESS:
          msivStove_image.setImageUrl(v_task.getSolve_image1());
          msivStove_image.setVisibility(View.VISIBLE);
          break;
      }
    }
  };


  private V_task v_task;
  private Button btnChoose_stove;
  private Button btnComment;
  private Button btnPay;
  private MySmartImageView msivStove_image;
  private EditText edComment;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comment);
    YCLTools.getInstance().setOnChoosePictureListener(this);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    v_task = (V_task) bundle.get("task");
    initView();
    initData();
  }

  private void initData() {
      btnComment.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          String detail = edComment.getText().toString().trim();
          if (Config.StringNoEmpty(detail)) {
            Comment comment = new Comment();
            comment.setCategory("1");
            comment.setDetail(detail);
            comment.setUserkey(MyApplication.getUserInstance().getId());
            comment.setTargetkey(v_task.getReq_id());
            new CreateComment(CommentActivity.this, comment, new CallBack() {
              @Override public void successed(int code, String message, boolean result) {
                if (result) {
                  Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_LONG).show();
                } else {
                  Toast.makeText(CommentActivity.this, "评论失败", Toast.LENGTH_LONG).show();
                }
              }

              @Override public void Failed() {
                Toast.makeText(CommentActivity.this, "网络异常", Toast.LENGTH_LONG).show();
              }
            });
          } else {
            Toast.makeText(CommentActivity.this, "请输入您的评论", Toast.LENGTH_LONG).show();
          }
        }
      });

    btnChoose_stove.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new UpLoadHeadImageDialog(CommentActivity.this).show();
      }
    });
    btnPay.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new UpdateTask_Order(CommentActivity.this, v_task, new CallBack() {
          @Override public void successed(int code, String message, boolean result) {
            if (result) {
              Toast.makeText(getApplicationContext(), "付款成功", Toast.LENGTH_LONG).show();
              PrefUtils.setString(CommentActivity.this,"taskid","");
              finish();
            } else {
              Toast.makeText(CommentActivity.this, "付款失败", Toast.LENGTH_LONG).show();
            }
          }

          @Override public void Failed() {

            Toast.makeText(CommentActivity.this, "网络异常", Toast.LENGTH_LONG).show();
          }
        });
      }
    });
  }

  private void initView() {
    btnChoose_stove= (Button) findViewById(R.id.btn_choose_stove);
    btnComment= (Button) findViewById(R.id.btn_comment);
    btnPay= (Button) findViewById(R.id.btn_pay);
    msivStove_image=(MySmartImageView)findViewById(R.id.msiv_stove_image);
    edComment=(EditText)findViewById(R.id.ed_comment);
  }

  @Override public void OnChoose(String filePath) {
    getToken(filePath);
  }

  @Override public void OnCancel() {

  }
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    YCLTools.getInstance().upLoadImage(requestCode, resultCode, data);
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
                v_task.setSolve_image1(imageUrl);
                Message msg=Message.obtain();
                msg.what=IMAGESUCCESS;
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
}
