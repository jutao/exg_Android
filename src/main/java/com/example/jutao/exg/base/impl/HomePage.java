package com.example.jutao.exg.base.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.MainActivity;
import com.example.jutao.exg.R;
import com.example.jutao.exg.TaskActivity;
import com.example.jutao.exg.Task_ListActivity;
import com.example.jutao.exg.WebViewActivity;
import com.example.jutao.exg.adapter.FailAdapter;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.bean.Advertisements;
import com.example.jutao.exg.service.AdressListener;
import com.example.jutao.exg.service.AdvertisemtsListener;
import com.example.jutao.exg.talk.TalkActivity;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.GetAdvertisements;
import com.example.jutao.exg.util.GetPositionBaidu;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.loopj.android.image.MySmartImageView;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页
 */
public class HomePage extends BasePager {
  public HomePage(Activity mActivity) {
    super(mActivity);
  }

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      vpService.setCurrentItem(msg.what % mAdvertisemeantList.size(), true);
    }
  };
  ImageView ivRedPoint;
  ViewPager vpService;
  ImageButton ibTong;
  ImageButton ibLock;
  ImageButton ibLight;
  ImageButton ibQuestion;
  LinearLayout llTask;

  MyOnClickListener myOnClickListener;
  List<Advertisements> mAdvertisemeantList;
  private ArrayList<ImageView> imageViews;
  LinearLayout llContainer;
  int mPointDis;
  //若没有网络加载以下图片
  private int[] mImageIds = { R.drawable.a01 };

  @Override public View initView() {
    View view = View.inflate(mActivity, R.layout.activity_service, null);
    tvTitle = (TextView) view.findViewById(R.id.tv_title);
    btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
    vpService = (ViewPager) view.findViewById(R.id.vp_service);
    ibTong = (ImageButton) view.findViewById(R.id.ib_tong);
    ibLock = (ImageButton) view.findViewById(R.id.ib_lock);
    ibLight = (ImageButton) view.findViewById(R.id.ib_light);
    ibQuestion = (ImageButton) view.findViewById(R.id.ib_question);
    llContainer = (LinearLayout) view.findViewById(R.id.ll_service_container);
    ivRedPoint = (ImageView) view.findViewById(R.id.iv_service_red_point);
    llTask = (LinearLayout) view.findViewById(R.id.ll_task);
    myOnClickListener = new MyOnClickListener();
    ibQuestion.setOnClickListener(myOnClickListener);
    ibTong.setOnClickListener(myOnClickListener);
    ibLock.setOnClickListener(myOnClickListener);
    ibLight.setOnClickListener(myOnClickListener);
    llTask.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(mActivity, Task_ListActivity.class);
        mActivity.startActivity(intent);
      }
    });
    new GetAdvertisements(mActivity, new AdvertisemtsListener() {
      @Override public void getAdvertisemts(final List<Advertisements> list) {

        if (list.size() > 0) {
          successList(list);
        } else {
          failed();
        }
      }

      @Override public void onFailed() {
        failed();
      }
    });

    return view;
  }

  @Override public void initData() {
    btnMenu.setVisibility(View.GONE);
    tvTitle.setText("我要报修");
  }

  private class MyOnClickListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      switch (v.getId()) {
        case R.id.ib_question:
          Intent intent = new Intent(mActivity, TalkActivity.class);
          mActivity.startActivity(intent);
          break;
        case R.id.ib_tong:
          jumptoTask("30");
          break;
        case R.id.ib_light:
          jumptoTask("20");
          break;
        case R.id.ib_lock:
          jumptoTask("10");
          break;
      }
    }
  }

  private void jumptoTask(String category) {
    Intent intent = new Intent(mActivity, TaskActivity.class);
    intent.putExtra("category", category);
    mActivity.startActivity(intent);
  }

  private class SuccessAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    @Override public int getCount() {
      return mAdvertisemeantList.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return object == view;
    }

    @Override public Object instantiateItem(ViewGroup container, final int position) {
      RelativeLayout.LayoutParams params =
          new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
              RelativeLayout.LayoutParams.MATCH_PARENT);

      container.setLayoutParams(params);
      MySmartImageView view = new MySmartImageView(mActivity);
      view.setImageUrl(Config.ADRESS + mAdvertisemeantList.get(position).getTop_pic1(),
          R.drawable.a01);
      view.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          String uri = mAdvertisemeantList.get(position).getUrl();
          Intent intent = new Intent(mActivity, WebViewActivity.class);
          intent.putExtra("uri", uri);
          mActivity.startActivity(intent);
        }
      });
      view.setScaleType(ImageView.ScaleType.FIT_XY);
      container.addView(view);
      return view;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    /**
     * 页面滑动过程中发生的回调
     * position:当前页面
     * positionOffset:移动偏移百分比
     * positionOffsetPixels：移动偏移像素
     */
    @Override public void onPageScrolled(int position, float positionOffset,
        int positionOffsetPixels) {
      RelativeLayout.LayoutParams params =
          (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
      int leftMargin = (int) (mPointDis * (position + positionOffset));
      params.leftMargin = leftMargin;
      ivRedPoint.setLayoutParams(params);
    }

    @Override public void onPageSelected(int position) {

    }

    @Override public void onPageScrollStateChanged(int state) {
      //当页面状态发生变化
    }
  }

  private void successList(List<Advertisements> list) {
    mAdvertisemeantList = list;
    //初始化ViewPager
    for (int i = 0; i < list.size(); i++) {
      //初始化小圆点
      ImageView point = new ImageView(mActivity);
      point.setImageResource(R.drawable.shape_point_gray);//设置图片(形状)
      if (i > 0) {
        //初始化布局参数，宽高包裹内容，父控件是谁，就是谁声明的布局参数
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //从第二个小圆点开始设置左边距
        params.leftMargin = 10;
        point.setLayoutParams(params);//设置布局参数
      }
      llContainer.addView(point);
      ivRedPoint.getViewTreeObserver()
          .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
            public void onGlobalLayout() {//layout方法执行结束后的回调

              //移除监听，避免重复回调
              ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
              mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
          });
    }
    SuccessAdapter adapter = new SuccessAdapter();
    vpService.setAdapter(adapter);
    vpService.setOnPageChangeListener(adapter);

    final Timer timer = new Timer();
    final TimerTask timeTask = new TimerTask() {
      int count = 0;

      @Override public void run() {
        Message msg = Message.obtain();
        msg.what = count;
        handler.sendMessage(msg);
        count++;
        if (count == 128) {
          timer.cancel();
        }
      }
    };
    timer.schedule(timeTask, 0, 5000);
  }

  public void failed() {
    //初始化ViewPager
    imageViews = new ArrayList<ImageView>();
    for (int i = 0; i < mImageIds.length; i++) {
      ImageView imageView = new ImageView(mActivity);
      imageView.setBackgroundResource(mImageIds[i]);//通过设置背景，可以让宽高填满布局
      //imageView.setImageResource(mImageIds[i]); 会按照图片自己本身的属性来填充
      imageViews.add(imageView);
    }
    ivRedPoint.setVisibility(View.INVISIBLE);
    FailAdapter failAdapter = new FailAdapter(imageViews);
    vpService.setAdapter(failAdapter);
  }
}
