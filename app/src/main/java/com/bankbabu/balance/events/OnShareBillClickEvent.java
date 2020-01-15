package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Bill;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class OnShareBillClickEvent {

  private final Bill bill;

  public OnShareBillClickEvent(final Bill bill) {
    this.bill = bill;
  }

  public Bill getBill() {
    return bill;
  }
}
