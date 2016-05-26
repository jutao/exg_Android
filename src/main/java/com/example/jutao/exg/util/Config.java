package com.example.jutao.exg.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.text.SimpleDateFormat;

public class Config {

  //public static final String ADRESS = "http://192.168.1.102:8080/exg";
  //public static final String ADRESS = "http://192.168.1.2:8080/exg";
  public static final String ADRESS = "http://172.18.192.3:8080/exg";
  //public static final String ADRESS = "http://172.18.13.2:8080/exg";
  //public static final String ADRESS = "http://192.168.22.151:8080/exg";
  //public static final String ADRESS = "http://192.168.0.109:8080/exg";
  //public static final String ADRESS = "http://192.168.253.3:8080/exg";
  //public static final String ADRESS="http://172.18.13.3:8080/exg";
  //public static final String ADRESS="http://10.0.3.2:8080/exg";

  public static final String AccessKey = "ISAnY7RsRru1E_kyQjsC2YgzYnFT_oKJqTJsL_Hg";
  public static final String SecretKey = "SHUgWm0j_5OAyTythxJAJFdaNUI8V7koHYPjWwCT";
  public static final String ZONE = "http://7xthjd.com1.z0.glb.clouddn.com/";

  /**
   * 判断字符串是否为空
   */
  public static boolean StringNoEmpty(String str) {
    if (str != null && !str.equals("")) return true;
    return false;
  }

  public static SimpleDateFormat getDateFormat() {

    return new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒");
  }


  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+50;
    listView.setLayoutParams(params);
  }
}
