package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Account;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public class OnDeleteAccountClickEvent {

  private final Account account;

  public OnDeleteAccountClickEvent(final Account item) {
    this.account = item;
  }

  public Account getItem() {
    return account;
  }
}
