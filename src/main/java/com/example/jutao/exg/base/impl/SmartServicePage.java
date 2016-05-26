package com.example.jutao.exg.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jutao.exg.Order_ListActivity;
import com.example.jutao.exg.QualificatActivity;
import com.example.jutao.exg.R;
import com.example.jutao.exg.base.BasePager;
import com.example.jutao.exg.bean.Order_master;
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.bean.Tasks;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.service.TaskListener;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.util.CreateOrder;
import com.example.jutao.exg.util.GetTask;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.loopj.android.image.MySmartImageView;
import com.loopj.android.image.SmartImageView;
import java.util.ArrayList;
import java.util.List;

public class SmartServicePage extends BasePager {
  public SmartServicePage(Activity mActivity) {
    super(mActivity);
  }

  private TextView tvTitle;
  private Button btnService_search;
  private ListView lvSearvice;
  private RelativeLayout rlOrder_detail;
  List<Tasks> mTaskList = new ArrayList<Tasks>();

  @Override public View initView() {
    View view = View.inflate(mActivity, R.layout.service_fragment, null);
    tvTitle = (TextView) view.findViewById(R.id.tv_title);
    btnService_search = (Button) view.findViewById(R.id.btn_service_search);
    lvSearvice = (ListView) view.findViewById(R.id.lv_searvice);
    rlOrder_detail=(RelativeLayout)view.findViewById(R.id.rl_order_detail);
    return view;
  }

  @Override public void initData() {
    tvTitle.setText("维修");
    btnService_search.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        final Task task = new Task();
        task.setCategory(MyApplication.getUserInstance().getUsertype());
        task.setStatus("10");
        new GetTask(mActivity, task, new TaskListener() {
          ListAdapter listAdapter = new ListAdapter();

          @Override public void getTasks(List<Tasks> list) {
            if (list.size() > 0) {
              mTaskList = list;
              lvSearvice.setAdapter(listAdapter);
              //设置总高度，解决嵌套显示不全的问题
              Config.setListViewHeightBasedOnChildren(lvSearvice);
            } else {
              if (mTaskList.size() > 0) {
                mTaskList = list;
                listAdapter.notifyDataSetChanged();
                lvSearvice.setAdapter(listAdapter);
              }
              Toast.makeText(mActivity, "附近没有需要维修的用户", Toast.LENGTH_LONG).show();
            }
          }

          @Override public void onFailed() {
            Toast.makeText(mActivity, "网络异常", Toast.LENGTH_LONG).show();
          }
        });
      }
    });
    rlOrder_detail.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent=new Intent(mActivity,Order_ListActivity.class);
        mActivity.startActivity(intent);
      }
    });
  }

  private class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    public  ListAdapter(){
      mInflater = LayoutInflater.from(mActivity);
    }
    @Override public int getCount() {
      return mTaskList.size();
    }

    @Override public Object getItem(int position) {
      return mTaskList.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(final int position, View convertView, ViewGroup parent) {
      final Tasks tasks=mTaskList.get(position);
      ViewHolder viewHolder = null;
      if (convertView == null) {
          convertView = mInflater.inflate(R.layout.tasklist_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.sivUser_icon= (SmartImageView) convertView.findViewById(R.id.siv_user_icon);
        viewHolder.tvCategory_name=(TextView)convertView.findViewById(R.id.tv_category_name);
        viewHolder.tvStatus_name=(TextView)convertView.findViewById(R.id.tv_status_name);
        viewHolder.tvUpdatetime=(TextView)convertView.findViewById(R.id.tv_updatetime);
        viewHolder.tvUserid_task=(TextView)convertView.findViewById(R.id.tv_userid_task);
        viewHolder.tvUserneck_task=(TextView)convertView.findViewById(R.id.tv_userneck_task);
        viewHolder.tvUserseat_task=(TextView)convertView.findViewById(R.id.tv_userseat_task);
        viewHolder.tvPro_description=(TextView)convertView.findViewById(R.id.tv_pro_description);
        viewHolder.msivProblem_image_task=(MySmartImageView)convertView.findViewById(R.id.msiv_problem_image_task);
        viewHolder.btnCall_task=(Button)convertView.findViewById(R.id.btn_call_task);
        viewHolder.btnGettask=(Button)convertView.findViewById(R.id.btn_gettask);
          if (Config.StringNoEmpty(tasks.getIcon())) {
            viewHolder.sivUser_icon.setImageUrl(tasks.getIcon());
          }
          if (Config.StringNoEmpty(tasks.getNickname())) {
            viewHolder.tvUserneck_task.setText(tasks.getNickname());
          }
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      // 设置数据
      viewHolder.tvCategory_name.setText(tasks.getCategoryName());
      viewHolder.tvStatus_name.setText(tasks.getStatusName());
      viewHolder.tvUserid_task.setText(tasks.getUser_id());
      viewHolder.tvUserseat_task.setText(tasks.getSeat());
      viewHolder.tvPro_description.setText(tasks.getPro_description());
      viewHolder.msivProblem_image_task.setImageUrl(tasks.getPro_image1());
      viewHolder.tvUpdatetime.setText(Config.getDateFormat().format(tasks.getUpdate_time()));
      viewHolder.msivProblem_image_task.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent=new Intent(mActivity, QualificatActivity.class);
          intent.putExtra("qualificat",tasks.getPro_image1());
          mActivity.startActivity(intent);
        }
      });
      viewHolder.btnCall_task.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +tasks.getUser_id()));
          mActivity.startActivity(intent);

        }
      });
      viewHolder.btnGettask.setOnClickListener(new View.OnClickListener() {

        @Override public void onClick(View v) {
          if(tasks.getUserkey().equals(MyApplication.getUserInstance().getId())){
            Toast.makeText(mActivity,"自己不能接自己的单",Toast.LENGTH_LONG).show();
          }
          else{
            Order_master order_master=new Order_master();
            order_master.setRepair_userkey(MyApplication.getUserInstance().getId());
            order_master.setTaskKey(tasks.getId());
            new CreateOrder(mActivity, order_master, new CallBack() {
              @Override public void successed(int code, String message, boolean result) {

                  if (result){
                    Toast.makeText(mActivity,"接单成功,请去你的接单表查看",Toast.LENGTH_LONG).show();
                    //PrefUtils.setString(mActivity, "order_master", message);
                    mTaskList.remove(position);
                    //模拟点击
                    btnService_search.performClick();

                  }else {
                    if(code!=0){
                      if(code==500){
                        Toast.makeText(mActivity,"您还有任务没有完成,请先完成当前任务！！",Toast.LENGTH_LONG).show();
                      }else if(code==600){
                        Toast.makeText(mActivity," 这单已经被人抢啦！！",Toast.LENGTH_LONG).show();
                      }
                    }else {
                      Toast.makeText(mActivity,"接单失败",Toast.LENGTH_LONG).show();
                    }
                  }
              }

              @Override public void Failed() {
                Toast.makeText(mActivity,"网络异常,接单失败",Toast.LENGTH_LONG).show();
              }
            });
          }

        }
      });
      return convertView;
    }


    private final class ViewHolder {

      SmartImageView sivUser_icon;
      TextView tvCategory_name;
      TextView tvStatus_name;
      TextView tvUpdatetime;
      TextView tvUserid_task;
      TextView tvUserneck_task;
      TextView tvUserseat_task;
      TextView tvPro_description;
      MySmartImageView msivProblem_image_task;
      Button btnCall_task;
      Button btnGettask;
    }
  }
}
