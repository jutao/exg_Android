package com.example.jutao.exg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.bean.V_task;
import com.example.jutao.exg.service.TaskListListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.GetTaskLists;
import com.example.jutao.exg.view.ToggleButton;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.util.ArrayList;
import java.util.List;

public class Task_ListActivity extends Activity {
  ToggleButton toogleButton;
  ListView listOrder_before;
  ListView listOrder_alerdy;

  List<V_task> mBeforeList = new ArrayList<V_task>();
  List<V_task> mAlerdyList = new ArrayList<V_task>();

  AlerdyListAdapter alerdyListAdapter;
  BeforeListAdapter beforeListAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tasklist);
    toogleButton = (ToggleButton) findViewById(R.id.tgbtn_task);
    listOrder_before = (ListView) findViewById(R.id.list_task_before);
    listOrder_alerdy = (ListView) findViewById(R.id.list_task_alerdy);
    iniitData();
  }

  private void iniitData() {

    final String beforeUrl = Config.ADRESS + "/service/gettaskbefore";
    final String alerdyUrl = Config.ADRESS + "/service/gettaskalerdy";

    Task task = new Task();
    task.setUserkey(MyApplication.getUserInstance().getId());
    new GetTaskLists(Task_ListActivity.this, task, beforeUrl, new TaskListListener() {
      @Override public void getTasks(List<V_task> list) {
        if (list.size() > 0) {
          mBeforeList = list;
          beforeListAdapter = new BeforeListAdapter();
          listOrder_before.setAdapter(beforeListAdapter);
          listOrder_before.setOnItemClickListener(beforeListAdapter);
        }
      }

      @Override public void onFailed() {

      }
    });
    new GetTaskLists(Task_ListActivity.this, task, alerdyUrl, new TaskListListener() {
      @Override public void getTasks(List<V_task> list) {
        if (list.size() > 0) {
          mAlerdyList = list;
          alerdyListAdapter = new AlerdyListAdapter();
          listOrder_alerdy.setAdapter(alerdyListAdapter);
          listOrder_alerdy.setOnItemClickListener(alerdyListAdapter);
          toogleButton.setClickable(true);
        }
      }

      @Override public void onFailed() {

      }
    });
    if (mAlerdyList.size() == 0) {
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
    final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibleList, "alpha", 0f, 1.0f);
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

  private class BeforeListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private LayoutInflater mInflater;

    public BeforeListAdapter() {
      mInflater = LayoutInflater.from(Task_ListActivity.this);
    }

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
      final V_task v_task = mBeforeList.get(position);
      ViewHolder viewHolder = null;
      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.list_task_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.tvTask_status_item =
            (TextView) convertView.findViewById(R.id.tv_task_status_item);
        viewHolder.tvTask_updatetime_item =
            (TextView) convertView.findViewById(R.id.tv_task_updatetime_item);
        viewHolder.tvTask_categoryname_item =
            (TextView) convertView.findViewById(R.id.tv_task_categoryname_item);
        viewHolder.tvReq_name = (TextView) convertView.findViewById(R.id.tv_req_name);
        viewHolder.tvReq_phone_item = (TextView) convertView.findViewById(R.id.tv_req_phone_item);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      // 设置数据
      viewHolder.tvTask_status_item.setText(v_task.getStatusName());
      viewHolder.tvTask_updatetime_item.setText(
          Config.getDateFormat().format(v_task.getUpdate_time()));
      viewHolder.tvTask_categoryname_item.setText(v_task.getCategoryName());
      viewHolder.tvReq_name.setText(v_task.getReq_name());
      viewHolder.tvReq_phone_item.setText(v_task.getReq_id());
      return convertView;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      switch (Integer.valueOf(mBeforeList.get(position).getStatus())){
        case 20:
          Intent intent=new Intent(Task_ListActivity.this,CommentActivity.class);
          Bundle bundle=new Bundle();
          bundle.putSerializable("task",mBeforeList.get(position));
          intent.putExtras(bundle);
          startActivity(intent);
          break;
      }
    }

    private final class ViewHolder {
      TextView tvTask_updatetime_item;
      TextView tvTask_status_item;
      TextView tvTask_categoryname_item;
      TextView tvReq_name;
      TextView tvReq_phone_item;
    }
  }

  private class AlerdyListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private LayoutInflater mInflater;

    public AlerdyListAdapter() {
      mInflater = LayoutInflater.from(Task_ListActivity.this);
    }

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
      final V_task v_task = mAlerdyList.get(position);
      ViewHolder viewHolder = null;
      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.list_task_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.tvTask_status_item =
            (TextView) convertView.findViewById(R.id.tv_task_status_item);
        viewHolder.tvTask_updatetime_item =
            (TextView) convertView.findViewById(R.id.tv_task_updatetime_item);
        viewHolder.tvTask_categoryname_item =
            (TextView) convertView.findViewById(R.id.tv_task_categoryname_item);
        viewHolder.tvReq_name = (TextView) convertView.findViewById(R.id.tv_req_name);
        viewHolder.tvReq_phone_item = (TextView) convertView.findViewById(R.id.tv_req_phone_item);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      // 设置数据
      viewHolder.tvTask_status_item.setText(v_task.getStatusName());
      viewHolder.tvTask_updatetime_item.setText(
          Config.getDateFormat().format(v_task.getUpdate_time()));
      viewHolder.tvTask_categoryname_item.setText(v_task.getCategoryName());
      viewHolder.tvReq_name.setText(v_task.getReq_name());
      viewHolder.tvReq_phone_item.setText(v_task.getReq_id());
      return convertView;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private final class ViewHolder {
      TextView tvTask_updatetime_item;
      TextView tvTask_status_item;
      TextView tvTask_categoryname_item;
      TextView tvReq_name;
      TextView tvReq_phone_item;
    }
  }
}
