package com.example.jutao.exg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.alibaba.fastjson.JSON;
import com.example.jutao.exg.bean.User;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.service.UserListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.GetUserByid;
import com.example.jutao.exg.util.JudgeLogin;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.volleydemo.MyApplication;

/**
 * 闪屏开始页面
 */
public class SplashActivity extends Activity {
  private RelativeLayout rlRoot;
  final int LOGINACTIVITY=1;
  final int MAINACTIVITY=2;
  final int GUIDEActivity=3;
  Context context;

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      Intent intent = null;
      switch (msg.what) {
        case LOGINACTIVITY:
          intent=new Intent(context,LoginActivity.class);
          break;
        case MAINACTIVITY:
          intent=new Intent(context,MainActivity.class);
          break;
        case GUIDEActivity:
          intent=new Intent(context,GuideActivity.class);
          break;
      }
      if(intent!=null){
        startActivity(intent);
        finish();
      }

    }
  };
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    context=this;
    initView();
    initAnimation();
  }

  /**
   * 旋转动画
   */
  private void initAnimation() {
    //一个绕自身中心点旋转360°的动画效果
    RotateAnimation animRotate =
        new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
    //设置时间为1000毫秒
    animRotate.setDuration(1000);
    //保持动画结束状态
    animRotate.setFillAfter(true);

    //缩放动画,从无到有，从中心点开始缩放
    ScaleAnimation animScale =
        new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
    animScale.setDuration(1000);
    animScale.setFillAfter(true);

    //渐变动画
    AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
    animAlpha.setDuration(2000);
    animAlpha.setFillAfter(true);

    //动画集合
    AnimationSet animationSet = new AnimationSet(true);
    animationSet.addAnimation(animRotate);
    animationSet.addAnimation(animScale);
    animationSet.addAnimation(animAlpha);

    //启动动画
    rlRoot.startAnimation(animationSet);
    animationSet.setAnimationListener(new Animation.AnimationListener() {

      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        Log.d("TAG", "endend");
        //动画结束，跳转页面。
        //如果是第一次跳到新手引导页面，否则跳到主界面
        Boolean isFirst = PrefUtils.getBoolean(context, "is_first_enter", true);
        if (isFirst) {
            Message msg=Message.obtain();
            msg.what=GUIDEActivity;
            handler.sendMessage(msg);
        } else {
          //从偏好设置中查看是否有记录的账号密码
          final String username=PrefUtils.getString(context,"username",null);
          final String password=PrefUtils.getString(context,"password",null);
          if(Config.StringNoEmpty(username)&&Config.StringNoEmpty(password)){

            String userString=PrefUtils.getString(context,"userInfo",null);
            //Log.d("Tag",userString);
            User user = JSON.parseObject(userString, User.class);
            //Log.d("Tag",user.getId());
            MyApplication.setUser(user);
            Message msg=Message.obtain();
            msg.what=MAINACTIVITY;
            handler.sendMessage(msg);
          }else{
            Message msg=Message.obtain();
            msg.what=LOGINACTIVITY;
            handler.sendMessage(msg);
          }
        }

      }

      @Override public void onAnimationRepeat(Animation animation) {
        //当动画重复时调用
      }
    });
  }

  /**
   * 视图初始化
   */
  private void initView() {
    rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
  }

}
