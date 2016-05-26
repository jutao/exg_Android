package com.example.jutao.exg.service;

import com.example.jutao.exg.bean.V_task;
import java.util.List;

public interface TaskListListener {
  public void getTasks(List<V_task> list);
  public void onFailed();
}
