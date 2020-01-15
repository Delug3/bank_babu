package com.bankbabu.balance.events;

import com.bankbabu.balance.models.State;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/28/19.
 */
public class OnStateItemClickEvent {

  private final State bank;

  public OnStateItemClickEvent(final State bank) {
    this.bank = bank;
  }

  public State getState() {
    return bank;
  }
}
