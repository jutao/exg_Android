package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.JudgeLogin;
import com.example.jutao.exg.util.PrefUtils;

/**
 * 闪屏开始页面
 */
public class SplashActivity extends Activity {
  private RelativeLayout rlRoot;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
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
        Boolean isFirst = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);

        final Intent[] intent = new Intent[1];
        if (isFirst) {
          intent[0] = new Intent(getApplicationContext(), GuideActivity.class);
        } else {
          //从偏好设置中查看是否有记录的账号密码
          String username=PrefUtils.getString(SplashActivity.this,"username",null);
          String password=PrefUtils.getString(SplashActivity.this,"password",null);
          if(username!=null&&!username.equals("")&&password!=null&&!password.equals("")){
            JudgeLogin judgeLogin=new JudgeLogin(SplashActivity.this, username, password, new JudgeListener() {
              @Override public void getJudge(boolean result) {
                Log.d("Tag",result+"");
                if(result){
                  intent[0] = new Intent(getApplicationContext(), MainActivity.class);
                }else{
                  intent[0] =new Intent(getApplicationContext(),LoginActivity.class);
                }
              }
            });
          }else{
            intent[0] =new Intent(getApplicationContext(),LoginActivity.class);
          }
        }
        startActivity(intent[0]);
        finish();
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
