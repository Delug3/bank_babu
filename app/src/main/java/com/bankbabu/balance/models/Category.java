package com.bankbabu.balance.models;

/**
 * Created by n9xCh on 29-Aug-16.
 */
public class Category {

  private String name;
  private int icon;
  private int id;

  public Category(String name, int icon) {
    this.name = name;
    this.icon = icon;
  }

  public Category() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
