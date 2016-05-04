package com.example.jutao.exg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.example.jutao.exg.MainActivity;
import com.example.jutao.exg.R;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.base.impl.HomePage;
import com.example.jutao.exg.base.impl.SettingPage;
import com.example.jutao.exg.base.impl.SmartServicePage;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.util.UpdateUser;
import com.example.jutao.exg.view.NoScroViewPager;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.isseiaoki.simplecropview.util.Logger;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ycl.chooseavatar.library.OnChoosePictureListener;
import com.ycl.chooseavatar.library.YCLTools;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentFragment extends BaseFragment implements OnChoosePictureListener {
  ArrayList<BasePager> mBasePagers;
  NoScroViewPager mViewPager;
  RadioGroup mRadioGroup;
  int thisPosition = 0;

  private enum PageNumber {HOMEPAGE, SMARTSERVICEPAGE, SETTINGPAGE}

  private static final int NETSTATUSNORMAL=1;
  private static final int NETSTATUSFAIL=2;
  private static final int UPDATEFAILL=3;

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      String text="";
      switch (msg.what) {
        case NETSTATUSNORMAL:
          text="设置成功";
          break;
        case NETSTATUSFAIL:
          text="网络异常";
          break;
        case UPDATEFAILL:
          text="更新失败";
          break;
      }
      Toast.makeText(getActivity(),text,Toast.LENGTH_LONG).show();
    }
  };

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    YCLTools.getInstance().setOnChoosePictureListener(this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public View initView() {
    View view = View.inflate(mActivity, R.layout.fragment_content, null);
    mViewPager = (NoScroViewPager) view.findViewById(R.id.novp_content);
    mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_content);

    return view;
  }

  @Override public void onResume() {
    if(null!=mBasePagers&&thisPosition==PageNumber.SETTINGPAGE.ordinal()){
      mBasePagers.get(PageNumber.SETTINGPAGE.ordinal()).initData();
    }
    super.onResume();

  }

  @Override public void initData() {
    mBasePagers = new ArrayList<BasePager>();
    mBasePagers.add(new HomePage(mActivity));
    mBasePagers.add(new SmartServicePage(mActivity));
    mBasePagers.add(new SettingPage(mActivity, this));
    mViewPager.setAdapter(new ContentPageAdapter());
    mViewPager.setOnPageChangeListener(new ContentPageAdapter());
    mRadioGroup.setOnCheckedChangeListener(new MyRadioGroupListener());

    //手动加载第一页数据
    mBasePagers.get(0).initData();
    setSlideMenuEnable(false);
  }

  private void setSlideMenuEnable(boolean enable) {
    MainActivity mainActivity = (MainActivity) mActivity;
    if (enable) {
      mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    } else {
      mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    YCLTools.getInstance().upLoadImage(requestCode, resultCode, data);
  }

  @Override public void OnChoose(String filePath) {
    //Toast.makeText(getActivity(), "filePath:" + filePath, Toast.LENGTH_LONG).show();
    getToken(filePath);
  }

  @Override public void OnCancel() {
    Toast.makeText(getActivity(), "OnCancel", Toast.LENGTH_LONG).show();
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
                if (PageNumber.SETTINGPAGE.ordinal() == thisPosition) {
                  MyApplication.user.setIcon(imageUrl);
                  //刷新数据
                  mBasePagers.get(PageNumber.SETTINGPAGE.ordinal()).initData();
                  userUpdate();
                }
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          }, null);
        }
      }
    }.start();
  }

  public void userUpdate(){
      String jsonString = JSON.toJSONString(MyApplication.user);
      PrefUtils.setString(getActivity(), "userInfo", jsonString);
      new UpdateUser(getActivity(), new JudgeListener() {
      @Override public void getJudge(boolean result) {
        Message message=Message.obtain();

        if (result) {
          message.what=NETSTATUSNORMAL;
        } else {
          message.what=UPDATEFAILL;
        }
        handler.sendMessage(message);
      }

      @Override public void Failed() {
        Message message=Message.obtain();
        message.what=NETSTATUSFAIL;
        handler.sendMessage(message);
      }
    });
  }
  private class MyRadioGroupListener implements RadioGroup.OnCheckedChangeListener {

    @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
      switch (checkedId) {
        case R.id.rb_home:
          mViewPager.setCurrentItem(0, false);//参数2代表是否有滑动动画,性能考虑填false
          break;
        case R.id.rb_smart:
          mViewPager.setCurrentItem(1, false);
          break;
        case R.id.rb_set:
          mViewPager.setCurrentItem(2, false);
          break;
      }
    }
  }

  private class ContentPageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    @Override public int getCount() {
      return mBasePagers.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      BasePager basePager = mBasePagers.get(position);
      View view = basePager.mRootView;
      container.addView(view);
      //basePager.initData();        ViewPager默认加载下一个页面，为了节省性能，不再此处加载初始化数据的方法
      return view;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
      thisPosition = position;
      BasePager basePager = mBasePagers.get(position);
      basePager.initData();

      //首页和设置页侧边栏禁用
      if (position == mBasePagers.size() - 1||position==0) {
        setSlideMenuEnable(false);
      } else {
        setSlideMenuEnable(true);
      }
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  }
}
