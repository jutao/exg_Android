package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.androidservice.TaskService;
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.service.AdressListener;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.CreateTask;
import com.example.jutao.exg.util.GetPositionBaidu;
import com.example.jutao.exg.util.PrefUtils;
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
import java.util.Date;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskActivity extends Activity implements OnChoosePictureListener {
  private Task task;
  private Button btnChoseproblem;
  private MySmartImageView msivProblem_image;
  private TextView tvTitle;
  private ImageButton ibTask_position;
  private EditText edTask_seat;
  private Button btnTask_submit;
  private EditText edProblem_detail;

  private static final int LOCKSMITHS = 10;
  private static final int ELECTRI = 20;
  private static final int CONDUIT = 30;
  private static final int UPLOADSUCCESS = 1;
  private static final int GETPOSITION = 2;

  private static final int VREATESUCCESS=3;
  private static final int VREATEFAILED=4;
  private static final int NETFAIL=5;
  private static final int ALERDY=6;

  Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case UPLOADSUCCESS:
          msivProblem_image.setImageUrl(task.getPro_image1());
          msivProblem_image.setVisibility(View.VISIBLE);
          break;
        case GETPOSITION:
          String position = msg.getData().getString("position");
          edTask_seat.setText(position);
          break;
        case VREATESUCCESS:
          Toast.makeText(getApplicationContext(),"发布任务成功,请等待维修员接单",Toast.LENGTH_LONG).show();
          Intent intent=new Intent(TaskActivity.this,TaskService.class);
          startService(intent);
          finish();
          break;
        case VREATEFAILED:
          Toast.makeText(TaskActivity.this,"发布任务失败",Toast.LENGTH_LONG).show();
          break;
        case NETFAIL:
          Toast.makeText(TaskActivity.this,"网络异常",Toast.LENGTH_LONG).show();
          break;
        case  ALERDY:
          Toast.makeText(TaskActivity.this,"您已经有正在发布的任务了，请解决再发布",Toast.LENGTH_LONG).show();
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_task);
    YCLTools.getInstance().setOnChoosePictureListener(this);
    initView();
    initData();
  }

  private void initView() {
    edProblem_detail = (EditText) findViewById(R.id.ed_problem_detail);
    msivProblem_image = (MySmartImageView) findViewById(R.id.msiv_problem_image);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    btnChoseproblem = (Button) findViewById(R.id.btn_choseproblem);
    btnChoseproblem.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new UpLoadHeadImageDialog(TaskActivity.this).show();
      }
    });
    ibTask_position = (ImageButton) findViewById(R.id.ib_task_position);
    edTask_seat = (EditText) findViewById(R.id.ed_task_seat);
    ibTask_position.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new GetPositionBaidu(TaskActivity.this, new AdressListener() {
          @Override public void getAdress(String city, String district, String street) {
            String text = city + district + street;
            Bundle bundle = new Bundle();
            bundle.putString("position", text);
            Message msg = Message.obtain();
            msg.what = GETPOSITION;
            msg.setData(bundle);
            handler.sendMessage(msg);
          }
        });
      }
    });
    btnTask_submit = (Button) findViewById(R.id.btn_task_submit);
    btnTask_submit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
          if(Config.StringNoEmpty(PrefUtils.getString(TaskActivity.this,"taskid",""))){
            Toast.makeText(TaskActivity.this,"您已经有正在发布的任务了，请解决再发布",Toast.LENGTH_LONG).show();
          }else{
            String seat = edTask_seat.getText().toString().trim();
            if (Config.StringNoEmpty(seat)) {
              task.setSeat(seat);
            }
            String proDescription = edProblem_detail.getText().toString().trim();
            if (Config.StringNoEmpty(proDescription)) {
              task.setPro_description(proDescription);
            }
            task.setUserkey(MyApplication.getUserInstance().getId());
            new CreateTask(TaskActivity.this, task, new CallBack() {
              @Override public void successed(int code, String message, boolean result) {
                Message msg=Message.obtain();
                if(result){
                  msg.what=VREATESUCCESS;
                  PrefUtils.setString(TaskActivity.this,"taskid",message);
                }else {
                  if(code==500){
                    msg.what=ALERDY;
                  }else{
                    msg.what=VREATEFAILED;
                  }
                }
                handler.sendMessage(msg);
              }

              @Override public void Failed() {
                Message msg=Message.obtain();
                msg.what=NETFAIL;
                handler.sendMessage(msg);
              }
            });
          }
      }
    });
  }

  private void initData() {
    task = new Task();
    Intent intent = getIntent();
    String category = intent.getStringExtra("category");
    task.setCategory(category);
    String title = "";
    switch (Integer.valueOf(category)) {
      case LOCKSMITHS:
        title = "请填写开锁维修单";
        break;
      case ELECTRI:
        title = "请填写电器保修表";
        break;
      case CONDUIT:
        title = "请填写管道报修单";
        break;
    }
    tvTitle.setText(title);
  }

  @Override public void OnChoose(String filePath) {
    //Toast.makeText(this,filePath,Toast.LENGTH_LONG).show();
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
                task.setPro_image1(imageUrl);
                Message msg = Message.obtain();
                msg.what = UPLOADSUCCESS;
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

  @Override public void OnCancel() {

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    YCLTools.getInstance().upLoadImage(requestCode, resultCode, data);
  }
}
