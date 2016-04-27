package com.example.jutao.exg.util;

import android.app.Activity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.example.jutao.exg.R;
import com.example.jutao.exg.volleydemo.MyApplication;

/**
 * LBS：获取当前位置
 */
public class GetPositionBaidu {
  Activity mActivity;

  public GetPositionBaidu(Activity mActivity) {

    this.mActivity = mActivity;
  }

  // 定位相关
  private LocationClient mLocationClient;
  private MyLocationListener myLocationListener;
  private boolean isFirstIn = true;
  private MyLocationConfiguration.LocationMode mLocationMode;

  // 经纬度
  private double mLatitude;
  private double mLongtitude;

  private BitmapDescriptor mIconLocation;

  private float mCurrentX;
  String adress;

  public void initMap() {
    // 初始化定位
    initLocation();
  }

  private void initLocation() {
    mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
    mLocationClient = new LocationClient(mActivity);
    myLocationListener = new MyLocationListener();
    mLocationClient.registerLocationListener(myLocationListener);

    LocationClientOption option = new LocationClientOption();
    // option.setAddrType("all");
    option.setCoorType("bd09ll");
    option.setIsNeedAddress(true);
    option.setOpenGps(true);
    option.setScanSpan(1000);
    mLocationClient.setLocOption(option);
    // 初始化图标
    mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);

    // 开启定位
    mLocationClient.start();
  }

  private class MyLocationListener implements BDLocationListener {

    @Override public void onReceiveLocation(final BDLocation location) {

      MyLocationData data = new MyLocationData.Builder().direction(mCurrentX)
          .accuracy(location.getRadius())
          .latitude(location.getLatitude())
          .longitude(location.getLongitude())
          .build();
      // 设置自定义图标
      MyLocationConfiguration config =
          new MyLocationConfiguration(mLocationMode, true, mIconLocation);
      // 更新经纬度
      mLatitude = location.getLatitude();
      mLongtitude = location.getLongitude();
      isFirstIn = false;
      //Toast.makeText(mActivity, location.getAddrStr(), Toast.LENGTH_LONG).show();
      mLocationClient.stop();
      MyApplication.adress = location.getAddrStr();
      //Log.d("TAG", MyApplication.getAdress());

    }
  }

}
