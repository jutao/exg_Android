package com.example.jutao.exg.bean;

import java.util.Date;

public class Comment {

  private String id = "";

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private String userkey = "";

  public String getUserkey() {
    return userkey;
  }

  public void setUserkey(String userkey) {
    this.userkey = userkey;
  }

  private String targetkey = "";

  public String getTargetkey() {
    return targetkey;
  }

  public void setTargetkey(String targetkey) {
    this.targetkey = targetkey;
  }

  private String detail = "";

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  private String category = "";

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  private String invalid = "";

  public String getInvalid() {
    return invalid;
  }

  public void setInvalid(String invalid) {
    this.invalid = invalid;
  }

  private Date register_time = null;

  public Date getRegister_time() {
    return register_time;
  }

  public void setRegister_time(Date register_time) {
    this.register_time = register_time;
  }

  private Date update_time = null;

  public Date getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(Date update_time) {
    this.update_time = update_time;
  }
}
