package com.example.jutao.exg.bean;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{

  private String id="";
  public String getId(){return id;}
  public void setId(String id){this.id = id;}

  private String userkey="";
  public String getUserkey(){return userkey;}
  public void setUserkey(String userkey){this.userkey = userkey;}

  private Double tip=null;
  public Double getTip(){return tip;}
  public void setTip(Double tip){this.tip = tip;}

  private Double epson=null;
  public Double getEpson(){return epson;}
  public void setEpson(Double epson){this.epson = epson;}

  private String pro_description="";
  public String getPro_description(){return pro_description;}
  public void setPro_description(String pro_description){this.pro_description = pro_description;}

  private String pro_image1="";
  public String getPro_image1(){return pro_image1;}
  public void setPro_image1(String pro_image1){this.pro_image1 = pro_image1;}

  private String pro_image2="";
  public String getPro_image2(){return pro_image2;}
  public void setPro_image2(String pro_image2){this.pro_image2 = pro_image2;}

  private String pro_image3="";
  public String getPro_image3(){return pro_image3;}
  public void setPro_image3(String pro_image3){this.pro_image3 = pro_image3;}

  private String pro_image4="";
  public String getPro_image4(){return pro_image4;}
  public void setPro_image4(String pro_image4){this.pro_image4 = pro_image4;}

  private String pro_image5="";
  public String getPro_image5(){return pro_image5;}
  public void setPro_image5(String pro_image5){this.pro_image5 = pro_image5;}

  private String solve_image1="";
  public String getSolve_image1(){return solve_image1;}
  public void setSolve_image1(String solve_image1){this.solve_image1 = solve_image1;}

  private String solve_image2="";
  public String getSolve_image2(){return solve_image2;}
  public void setSolve_image2(String solve_image2){this.solve_image2 = solve_image2;}

  private String solve_image3="";
  public String getSolve_image3(){return solve_image3;}
  public void setSolve_image3(String solve_image3){this.solve_image3 = solve_image3;}

  private String seat="";
  public String getSeat(){return seat;}
  public void setSeat(String seat){this.seat = seat;}

  private String status="";
  public String getStatus(){return status;}
  public void setStatus(String status){this.status = status;}

  private String statusName="";
  public String getStatusName(){return statusName;}
  public void setStatusName(String statusName){this.statusName = statusName;}

  private String invalid="";
  public String getInvalid(){return invalid;}
  public void setInvalid(String invalid){this.invalid = invalid;}

  private Date update_time=null;
  public Date getUpdate_time(){return update_time;}
  public void setUpdate_time(Date update_time){this.update_time = update_time;}

  private String category="";
  public String getCategory(){return category;}
  public void setCategory(String category){this.category = category;}

  private String categoryName="";
  public String getCategoryName(){return categoryName;}
  public void setCategoryName(String categoryName){this.categoryName = categoryName;}

}
