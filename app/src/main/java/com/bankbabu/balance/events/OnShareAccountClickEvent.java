package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Account;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public class OnShareAccountClickEvent {

  private final Account account;

  public OnShareAccountClickEvent(final Account item) {
    this.account = item;
  }

  public Account getAccount() {
    return account;
  }
}
