package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Tool;


public class OnToolItemClickEvent {

  private final Tool tool;

  public OnToolItemClickEvent(final Tool tool) {
    this.tool = tool;
  }

  public Tool getTool() {
    return tool;
  }
}
