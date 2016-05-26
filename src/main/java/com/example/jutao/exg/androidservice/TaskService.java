package com.example.jutao.exg.androidservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.jutao.exg.R;
import com.example.jutao.exg.Task_ListActivity;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.util.CheckTask;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TIAN on 2016/5/10.
 */
public class TaskService extends Service {
  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onStart(final Intent intent, int startId) {
    super.onStart(intent, startId);
    final Timer timer = new Timer();
    final TimerTask timeTask = new TimerTask() {
      @Override public void run() {
        new CheckTask(getApplicationContext(), new CallBack() {
          @Override public void successed(int code, String message, boolean result) {
            if (result) {
              //已经被接单
              Log.d("Tag", "已经接单");
              showNotification();

              timer.cancel();
              Intent intent1 = new Intent(getApplicationContext(), Service.class);
              getApplicationContext().stopService(intent);
            } else {
              Log.d("Tag", "继续请求");
            }
          }

          @Override public void Failed() {

          }
        });
      }
    };
    timer.schedule(timeTask, 0, 5000);
  }

  private void showNotification() {
    Intent myIntent=new Intent(getApplicationContext(), Task_ListActivity.class);

    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
    mBuilder.setContentTitle("您的任务已被接收")//设置通知栏标题
        .setContentText("请去您发布的任务列表查看")
            //  .setNumber(number) //设置通知集合的数量
        .setTicker("您的任务被接收啦！！！") //通知首次出现在通知栏，带上升动画效果的
        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
            //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
        .setOngoing(
            false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
        .setDefaults(
            Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
            //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        .setSmallIcon(R.drawable.icon);//设置通知小ICON
    Notification notification = mBuilder.build();
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    PendingIntent padingIntent=PendingIntent.getActivity(getApplicationContext(), 0, myIntent, 0);
    notification.contentIntent=padingIntent;
    mNotificationManager.notify(notification.flags , notification);
  }
}
