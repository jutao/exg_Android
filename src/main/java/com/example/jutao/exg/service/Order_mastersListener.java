package com.example.jutao.exg.service;

import com.example.jutao.exg.bean.Order_masters;
import java.util.List;

public interface Order_mastersListener {
  public void getOrder_masters(List<Order_masters> list);
  public void onFailed();
}
