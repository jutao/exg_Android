package com.example.jutao.exg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.jutao.exg.bean.Order_master;
import com.example.jutao.exg.bean.Order_masters;
import com.example.jutao.exg.service.Order_mastersListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.GetOrder_masters;
import com.example.jutao.exg.view.ToggleButton;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order_ListActivity extends Activity {
  ToggleButton toogleButton;
  ListView listOrder_before;
  ListView listOrder_alerdy;
  AlerdyListAdapter alerdyListAdapter;
  BeforeListAdapter beforeListAdapter;
  List<Order_masters> mBeforeList=new ArrayList<Order_masters>();
  List<Order_masters> mAlerdyList=new ArrayList<Order_masters>();
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.orderlist);
    toogleButton= (ToggleButton) findViewById(R.id.tgbtn_order);
    listOrder_before = (ListView) findViewById(R.id.list_order_before);
    listOrder_alerdy = (ListView) findViewById(R.id.list_order_alerdy);
    iniitData();

  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB) private void iniitData() {

    final String beforeUrl= Config.ADRESS+"/service/getordermasterbefore";
    final String alerdyUrl= Config.ADRESS+"/service/getordermasteralready";
    Order_master order_master=new Order_master();
    order_master.setRepair_userkey(MyApplication.getUserInstance().getId());

    new GetOrder_masters(Order_ListActivity.this, order_master, beforeUrl, new Order_mastersListener() {
      @Override public void getOrder_masters(List<Order_masters> list) {
        if(list.size()>0){
          mBeforeList=list;
          beforeListAdapter=new BeforeListAdapter();
          listOrder_before.setAdapter(beforeListAdapter);
          listOrder_before.setOnItemClickListener(beforeListAdapter);
        }

      }

      @Override public void onFailed() {

      }
    });
    new GetOrder_masters(Order_ListActivity.this, order_master, alerdyUrl, new Order_mastersListener() {
      @Override public void getOrder_masters(List<Order_masters> list) {
        if(list.size()>0){
          mAlerdyList=list;
          alerdyListAdapter=new AlerdyListAdapter();
          listOrder_alerdy.setAdapter(alerdyListAdapter);
          listOrder_alerdy.setOnItemClickListener(alerdyListAdapter);
          toogleButton.setClickable(true);
        }
      }

      @Override public void onFailed() {

      }
    });

    if(mAlerdyList.size()==0){
      toogleButton.setClickable(false);
    }

    toogleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
      @Override public void onToggle(boolean on) {
        flipit();
      }
    });
  }

  private Interpolator accelerator = new AccelerateInterpolator();
  private Interpolator decelerator = new DecelerateInterpolator();
  @TargetApi(Build.VERSION_CODES.HONEYCOMB) private void flipit() {
    //定义两个ListView，作用是充当容器
    final ListView visibleList;
    final ListView invisibleList;
    //如果beforeList为不可见的
    if (listOrder_before.getVisibility() == View.GONE) {
      //可见List存储alerdyList
      visibleList = listOrder_alerdy;
      //不可见List存储beforeList
      invisibleList = listOrder_before;
    } else {//相反
      invisibleList = listOrder_alerdy;
      visibleList = listOrder_before;
    }
    ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleList, "alpha", 1f, 0f);
    visToInvis.setDuration(500);
    //在动画开始的地方速率改变比较慢，然后开始加速
    visToInvis.setInterpolator(accelerator);
    final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibleList, "alpha",
        0f, 1.0f);
    invisToVis.setDuration(500);
    //在动画开始的地方快然后慢
    invisToVis.setInterpolator(decelerator);
    //给可见List设置一个动画监听
    visToInvis.addListener(new AnimatorListenerAdapter() {
      @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
      public void onAnimationEnd(Animator anim) {
        //当动画执行结束时，将可见List设置为不可见
        visibleList.setVisibility(View.GONE);
        //启动不可见List动画
        invisToVis.start();
        //将不可见List设置为可见的
        invisibleList.setVisibility(View.VISIBLE);
      }
    });
    //启动可见List的动画
    visToInvis.start();
  }
  private class BeforeListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private LayoutInflater mInflater;
    public BeforeListAdapter(){mInflater = LayoutInflater.from(Order_ListActivity.this);}
    @Override public int getCount() {
      return mBeforeList.size();
    }

    @Override public Object getItem(int position) {
      return mBeforeList.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      final Order_masters order_masters=mBeforeList.get(position);
      ViewHolder viewHolder = null;
      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.list_order_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.tvOrder_status_item=(TextView)convertView.findViewById(R.id.tv_order_status_item);
        viewHolder.tvUpdatetime_item=(TextView)convertView.findViewById(R.id.tv_updatetime_item);
        viewHolder.tvCategoryname_item=(TextView)convertView.findViewById(R.id.tv_categoryname_item);
        viewHolder.tvTelephome_item=(TextView)convertView.findViewById(R.id.tv_telephome_item);
        viewHolder.tvSeat_item=(TextView)convertView.findViewById(R.id.tv_seat_item);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      // 设置数据
      viewHolder.tvOrder_status_item.setText(order_masters.getStatusName());
      viewHolder.tvUpdatetime_item.setText(Config.getDateFormat().format(order_masters.getUpdate_time()));
      viewHolder.tvCategoryname_item.setText(order_masters.getTask().getCategoryName());
      viewHolder.tvTelephome_item.setText(order_masters.getTask_userid());
      viewHolder.tvSeat_item.setText(order_masters.getTask().getSeat());
      return convertView;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (Integer.valueOf(mBeforeList.get(position).getStatus())){
          case 10:
            Intent intent=new Intent(Order_ListActivity.this,AlerdyActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("order_master",mBeforeList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
            break;
          case 30:

            break;
        }
    }

    private final class ViewHolder {
      TextView tvUpdatetime_item;
      TextView tvOrder_status_item;
      TextView tvCategoryname_item;
      TextView tvTelephome_item;
      TextView tvSeat_item;
    }
  }
  private class AlerdyListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

    private LayoutInflater mInflater;
    public AlerdyListAdapter(){mInflater = LayoutInflater.from(Order_ListActivity.this);}
    @Override public int getCount() {
      return mAlerdyList.size();
    }

    @Override public Object getItem(int position) {
      return mAlerdyList.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      final Order_masters order_masters=mAlerdyList.get(position);
      ViewHolder viewHolder = null;
      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.list_order_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.tvOrder_status_item=(TextView)convertView.findViewById(R.id.tv_order_status_item);
        viewHolder.tvUpdatetime_item=(TextView)convertView.findViewById(R.id.tv_updatetime_item);
        viewHolder.tvCategoryname_item=(TextView)convertView.findViewById(R.id.tv_categoryname_item);
        viewHolder.tvTelephome_item=(TextView)convertView.findViewById(R.id.tv_telephome_item);
        viewHolder.tvSeat_item=(TextView)convertView.findViewById(R.id.tv_seat_item);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      // 设置数据
      viewHolder.tvOrder_status_item.setText(order_masters.getStatusName());
      viewHolder.tvUpdatetime_item.setText(Config.getDateFormat().format(order_masters.getUpdate_time()));
      viewHolder.tvCategoryname_item.setText(order_masters.getTask().getCategoryName());
      viewHolder.tvTelephome_item.setText(order_masters.getTask_userid());
      viewHolder.tvSeat_item.setText(order_masters.getTask().getSeat());
      return convertView;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private final class ViewHolder {
      TextView tvUpdatetime_item;
      TextView tvOrder_status_item;
      TextView tvCategoryname_item;
      TextView tvTelephome_item;
      TextView tvSeat_item;
    }
  }
}
