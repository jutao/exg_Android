package com.example.jutao.exg.service;

import com.example.jutao.exg.bean.Tasks;
import java.util.List;

public interface TaskListener {
  public void getTasks(List<Tasks> list);
  public void onFailed();
}
