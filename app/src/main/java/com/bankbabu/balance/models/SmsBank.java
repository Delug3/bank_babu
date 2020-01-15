package com.bankbabu.balance.models;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public class SmsBank {

  private int id;
  private String title;
  private String bankName;
  private String message;
  private String info;
  private String phoneNumber;

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(final String bankName) {
    this.bankName = bankName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(final String info) {
    this.info = info;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(final String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
