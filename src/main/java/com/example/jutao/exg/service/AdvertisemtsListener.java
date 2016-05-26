package com.example.jutao.exg.service;

import com.example.jutao.exg.bean.Advertisements;
import java.util.List;

/**
 * Created by TIAN on 2016/5/4.
 */
public interface AdvertisemtsListener {
  public void getAdvertisemts(List<Advertisements> list);
  public void onFailed();
}
