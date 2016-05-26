package com.example.jutao.exg;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.jutao.exg.util.PrefUtils;
import java.util.ArrayList;

/**
 * 新手引导页面
 */
public class GuideActivity extends Activity {
  private ViewPager vpGuide;
  private ArrayList<ImageView> imageViews;
  LinearLayout llContainer;
  ImageView ivRedPoint;
  GuideAdapter guideAdapter;
  Button btnStart;
  int mPointDis;
  //引导页图片数组
  private int[] mImageIds = { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_guide);
    initView();
  }

  private void initView() {
    vpGuide = (ViewPager) findViewById(R.id.vp_guide);
    llContainer = (LinearLayout) findViewById(R.id.ll_container);
    ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
    btnStart=(Button)findViewById(R.id.btn_start);
    initData();
    guideAdapter = new GuideAdapter();
    vpGuide.setAdapter(guideAdapter);
    vpGuide.setOnPageChangeListener(guideAdapter);

    // 计算两个圆点的距离
    // 移动距离=第二个圆点left值 - 第一个圆点left值
    // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
    // mPointDis = llContainer.getChildAt(1).getLeft()
    // - llContainer.getChildAt(0).getLeft();
    // System.out.println("圆点距离:" + mPointDis);

    //因为获取控件属性要在Activity的oncreate方法结束之后才可以，所以需要通过视图树来获取
    //观察者设计模式
    ivRedPoint.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
          @Override
          public void onGlobalLayout() {//layout方法执行结束后的回调

            //移除监听，避免重复回调
            ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
          }
        });
    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
      }
    });
  }

  /**
   * 初始化数据
   */
  private void initData() {
    //初始化ViewPager
    imageViews = new ArrayList<ImageView>();
    for (int i = 0; i < mImageIds.length; i++) {
      ImageView imageView = new ImageView(this);
      imageView.setBackgroundResource(mImageIds[i]);//通过设置背景，可以让宽高填满布局
      //imageView.setImageResource(mImageIds[i]); 会按照图片自己本身的属性来填充
      imageViews.add(imageView);

      //初始化小圆点
      ImageView point = new ImageView(this);
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
    }
  }

  private class GuideAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    @Override public int getCount() {
      return imageViews.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return object == view;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      ImageView view = imageViews.get(position);
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
      int leftMargin= (int) (mPointDis*(position+positionOffset));
      params.leftMargin=leftMargin;
      ivRedPoint.setLayoutParams(params);
    }

    @Override public void onPageSelected(int position) {
      //当某个页面被选中
      if(position==mImageIds.length-1){
        btnStart.setVisibility(View.VISIBLE);
      }else{
        btnStart.setVisibility(View.INVISIBLE);
      }
    }

    @Override public void onPageScrollStateChanged(int state) {
      //当页面状态发生变化
    }
  }
}
