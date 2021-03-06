package com.example.jutao.exg.talk;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.jutao.exg.R;
import com.example.jutao.exg.talk.bean.ChatMessage;
import com.example.jutao.exg.util.Config;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.loopj.android.image.SmartImageView;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
  private LayoutInflater mInflater;
  private List<ChatMessage> mDatas;

  public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
    mInflater = LayoutInflater.from(context);
    this.mDatas = mDatas;
  }

  @Override public int getCount() {
    return mDatas.size();
  }

  @Override public Object getItem(int position) {
    return mDatas.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemViewType(int position) {
    ChatMessage chatMessage = mDatas.get(position);
    if (chatMessage.getType() == ChatMessage.Type.INCOMING) {
      return 0;
    }
    return 1;
  }

  @Override public int getViewTypeCount() {
    return 2;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ChatMessage chatMessage = mDatas.get(position);
    ViewHolder viewHolder = null;
    if (convertView == null) {
      // 通过ItemType设置不同的布局
      if (getItemViewType(position) == 0) {
        convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_form_msg_date);
        viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
      } else {
        convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_date);
        viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
        viewHolder.mNeck = (TextView) convertView.findViewById(R.id.tv_talk_neckname);
        viewHolder.mImage = (SmartImageView) convertView.findViewById(R.id.iv_talk_icon);
        if (Config.StringNoEmpty(MyApplication.getUserInstance().getIcon())) {
          viewHolder.mImage.setImageUrl(MyApplication.getUserInstance().getIcon());
        }
        if (Config.StringNoEmpty(MyApplication.getUserInstance().getNickname())) {
          viewHolder.mNeck.setText(MyApplication.getUserInstance().getNickname());
        }
      }
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    // 设置数据
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    viewHolder.mDate.setText(df.format(chatMessage.getDate()));
    viewHolder.mMsg.setText(Html.fromHtml(chatMessage.getMsg()));
    return convertView;
  }

  private final class ViewHolder {

    SmartImageView mImage;
    TextView mDate;
    TextView mMsg;
    TextView mNeck;
  }
}
