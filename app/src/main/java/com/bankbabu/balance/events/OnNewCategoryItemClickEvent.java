package com.bankbabu.balance.events;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class OnNewCategoryItemClickEvent {

  private final Integer item;

  public OnNewCategoryItemClickEvent(final Integer item) {
    this.item = item;
  }

  public Integer getItem() {
    return item;
  }
}
