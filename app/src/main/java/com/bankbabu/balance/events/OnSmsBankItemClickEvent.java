package com.bankbabu.balance.events;

import com.bankbabu.balance.models.SmsBank;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/28/19.
 */
public class OnSmsBankItemClickEvent {

  private final SmsBank bank;

  public OnSmsBankItemClickEvent(final SmsBank bank) {
    this.bank = bank;
  }

  public SmsBank getBank() {
    return bank;
  }
}
