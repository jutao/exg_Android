package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.bean.Order_master;
import com.example.jutao.exg.bean.Order_masters;
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.UpdateOrder_taskTo_30;
import com.loopj.android.image.MySmartImageView;
import com.loopj.android.image.SmartImageView;

public class AlerdyActivity extends Activity {
  private Order_masters order_master;
  private SmartImageView sivUser_icon;
  private TextView tvCategory_name;
  private TextView tvStatus_name;
  private TextView tvUpdatetime;
  private TextView tvUserid_task;
  private TextView tvUserneck_task;
  private TextView tvUserseat_task;
  private TextView tvPro_description;
  private MySmartImageView msivProblem_image_task;
  private Button btnCall_task;
  private Button btnGettask;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alerdy);
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    order_master = (Order_masters) bundle.get("order_master");
    initView();
    initData();
  }

  private void initData() {
    if (Config.StringNoEmpty(order_master.getTask_usericon())) {
      sivUser_icon.setImageUrl(order_master.getTask_usericon());
    }
    if (Config.StringNoEmpty(order_master.getTask().getCategoryName())) {
      tvCategory_name.setText(order_master.getTask().getCategoryName());
    }
    if (Config.StringNoEmpty(order_master.getTask().getStatusName())) {
      tvStatus_name.setText(order_master.getTask().getStatusName());
    }
    if (Config.StringNoEmpty(order_master.getTask().getUpdate_time().toString())) {
      tvUpdatetime.setText(Config.getDateFormat().format(order_master.getTask().getUpdate_time()));
    }
    if(Config.StringNoEmpty(order_master.getTask_userid())){
      tvUserid_task.setText(order_master.getTask_userid());
    }
    if(Config.StringNoEmpty(order_master.getTask_user_name())){
      tvUserneck_task.setText(order_master.getTask_userid());
    }else {
      tvUserneck_task.setVisibility(View.INVISIBLE);
    }
    if(Config.StringNoEmpty(order_master.getTask().getSeat())){
      tvUserseat_task.setText(order_master.getTask().getSeat());
    }
    if(Config.StringNoEmpty(order_master.getTask().getPro_description())){
      tvPro_description.setText(order_master.getTask().getPro_description());
    }
    if(Config.StringNoEmpty(order_master.getTask().getPro_image1())){
      msivProblem_image_task.setImageUrl(order_master.getTask().getPro_image1());
    }
    btnCall_task.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order_master.getTask_userid()));
       startActivity(intent);

      }
    });
    msivProblem_image_task.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent=new Intent(AlerdyActivity.this, QualificatActivity.class);
        intent.putExtra("qualificat",order_master.getTask().getPro_image1());
        startActivity(intent);
      }
    });
    btnGettask.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new UpdateOrder_taskTo_30(AlerdyActivity.this, order_master, new CallBack() {
          @Override public void successed(int code, String message, boolean result) {
            if(result){
              Toast.makeText(getApplicationContext(),"任务进度已更新",Toast.LENGTH_LONG).show();
              finish();
            }else{
              Toast.makeText(AlerdyActivity.this,"更新状态失败",Toast.LENGTH_LONG).show();
            }
          }

          @Override public void Failed() {
            Toast.makeText(AlerdyActivity.this,"网络异常",Toast.LENGTH_LONG).show();
          }
        });
      }
    });
  }

  private void initView() {
    sivUser_icon = (SmartImageView) findViewById(R.id.siv_user_icon_order_alerdy);
    tvCategory_name = (TextView) findViewById(R.id.tv_category_name_order_alerdy);
    tvStatus_name = (TextView) findViewById(R.id.tv_status_name_order_alerdy);
    tvUpdatetime = (TextView) findViewById(R.id.tv_updatetime_order_alerdy);
    tvUserid_task = (TextView) findViewById(R.id.tv_userid_task_order_alerdy);
    tvUserneck_task = (TextView) findViewById(R.id.tv_userneck_task_order_alerdy);
    tvUserseat_task = (TextView) findViewById(R.id.tv_userseat_task_order_alerdy);
    tvPro_description = (TextView) findViewById(R.id.tv_pro_description_order_alerdy);
    msivProblem_image_task =
        (MySmartImageView) findViewById(R.id.msiv_problem_image_task_order_alerdy);
    btnCall_task = (Button) findViewById(R.id.btn_call_task_order_alerdy);
    btnGettask = (Button) findViewById(R.id.btn_sendtask_order_alerdy);
  }
}
