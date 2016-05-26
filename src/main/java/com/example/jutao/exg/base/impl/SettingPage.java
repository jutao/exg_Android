package com.example.jutao.exg.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.LoginActivity;
import com.example.jutao.exg.NeckActivity;
import com.example.jutao.exg.QualificatActivity;
import com.example.jutao.exg.R;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.bean.User;
import com.example.jutao.exg.dialog.GenderDialog;
import com.example.jutao.exg.fragment.BaseFragment;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.PrefUtils;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.loopj.android.image.SmartImageView;
import com.ycl.chooseavatar.library.UpLoadHeadImageDialog;

/**
 * 我的账号
 */
public class SettingPage extends BasePager {
  Fragment fragment;
  public SettingPage(Activity mActivity,BaseFragment fragment) {
    super(mActivity);
    this.fragment=fragment;
  }

  RelativeLayoutClickListener relativeLayoutClickListener;

  private RelativeLayout out_login;
  private RelativeLayout rl_Head_image;
  //private RelativeLayout rl_Myadress;
  private RelativeLayout rl_Gender;
  private RelativeLayout rl_Neck_name;
  private RelativeLayout rlQualificat;

  private SmartImageView smart_Head_image;
  private TextView tv_Neck_name;
  private TextView tv_User_id;
  private TextView tv_Gender;
  private TextView tv_Category;
  private TextView tv_User_type;

  @Override public View initView() {
    View view = View.inflate(mActivity, R.layout.activity_setting, null);
    tvTitle = (TextView) view.findViewById(R.id.tv_title);
    btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
    out_login = (RelativeLayout) view.findViewById(R.id.rl_out_login);
    rl_Neck_name = (RelativeLayout) view.findViewById(R.id.rl_neck_name);

    relativeLayoutClickListener = new RelativeLayoutClickListener();
    rl_Head_image = (RelativeLayout) view.findViewById(R.id.rl_head_image);
    rl_Head_image.setOnClickListener(relativeLayoutClickListener);
    rl_Neck_name.setOnClickListener(relativeLayoutClickListener);
    smart_Head_image = (SmartImageView) view.findViewById(R.id.smart_head_image);
    tv_Neck_name = (TextView) view.findViewById(R.id.tv_neck_name);
    tv_User_id = (TextView) view.findViewById(R.id.tv_user_id);
    //rl_Myadress = (RelativeLayout) view.findViewById(R.id.rl_myadress);
    //rl_Myadress.setOnClickListener(relativeLayoutClickListener);
    rl_Gender = (RelativeLayout) view.findViewById(R.id.rl_gender);
    rl_Gender.setOnClickListener(relativeLayoutClickListener);
    tv_Gender = (TextView) view.findViewById(R.id.tv_gender);
    tv_Category = (TextView) view.findViewById(R.id.tv_category);
    tv_User_type = (TextView) view.findViewById(R.id.tv_user_type);
    rlQualificat=(RelativeLayout)view.findViewById(R.id.rl_qualificat);
    rlQualificat.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(Config.StringNoEmpty(MyApplication.getUserInstance().getQualificat())&&
            MyApplication.getUserInstance().getCategory().equals("1")){
          Intent intent=new Intent(mActivity, QualificatActivity.class);
          intent.putExtra("qualificat",MyApplication.getUserInstance().getQualificat());
          mActivity.startActivity(intent);
        }else{
          Toast.makeText(mActivity,"您还不是维修员",Toast.LENGTH_LONG).show();
        }
      }
    });

    out_login.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //退出登录
        PrefUtils.setString(mActivity, "username", "");
        PrefUtils.setString(mActivity, "password", "");
        PrefUtils.setString(mActivity, "userInfo", "");
        PrefUtils.setString(mActivity, "taskid", "");

        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
      }
    });
    return view;
  }

  @Override public void initData() {
    //设置页面设置menu按钮不可见，不需要侧滑菜单
    btnMenu.setVisibility(View.GONE);
    tvTitle.setText("设置");
    User user = MyApplication.getUserInstance();
    if (Config.StringNoEmpty(user.getIcon())) {
      smart_Head_image.setImageUrl(user.getIcon());
    }
    if (Config.StringNoEmpty(user.getNickname())) {
      tv_Neck_name.setText(user.getNickname());
    }
    if (Config.StringNoEmpty(user.getUserid())) {
      tv_User_id.setText(user.getUserid());
    }
    if (Config.StringNoEmpty(user.getGender())) {
      tv_Gender.setText(user.getGenderName());
    }
    if (Config.StringNoEmpty(user.getCategory())) {
      tv_Category.setText(user.getCategoryName());
    }
    if (Config.StringNoEmpty(user.getUsertype())) {
      tv_User_type.setText(user.getUsertypeName());
    }
  }

  private class RelativeLayoutClickListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      switch (v.getId()) {
        case R.id.rl_head_image:
          new UpLoadHeadImageDialog(mActivity,fragment).show();
          break;
        case R.id.rl_neck_name:
          Intent intent=new Intent(mActivity, NeckActivity.class);
          mActivity.startActivity(intent);
          break;
        //case R.id.rl_myadress:
        //  Log.d("Tag", "我的地址");
        //  break;
        case R.id.rl_gender:
          new GenderDialog(mActivity,fragment).show();
          break;
      }
    }
  }
}
