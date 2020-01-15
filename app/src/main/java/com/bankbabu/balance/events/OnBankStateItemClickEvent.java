package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Bank;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/28/19.
 */
public class OnBankStateItemClickEvent {

  private final Bank bank;

  public OnBankStateItemClickEvent(final Bank bank) {
    this.bank = bank;
  }

  public Bank getBank() {
    return bank;
  }
}
