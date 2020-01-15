package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Category;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class OnDeleteCategoryEvent {

  private final Category category;

  public OnDeleteCategoryEvent(final Category item) {
    this.category = item;
  }

  public Category getCategory() {
    return category;
  }
}
