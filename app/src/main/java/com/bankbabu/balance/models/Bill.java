package com.bankbabu.balance.models;

import android.content.Context;
import com.bankbabu.balance.R;
import java.util.List;

/**
 * Created by New Age on 06-02-2018.
 */
public class Bill {

  private int id;
  private String categoryName;
  private int categoryIcon;
  private String payee;
  private String amount;
  private String notes;
  private String dueDate;
  private String billType;
  private int customDueDate;
  private int repeat;
  private int status;
  private String repeatEvery;
  private String nextDueDate;
  private String notification;
  private RepeatBy repeatByType;

  private List<Bill> childBillList;

  public Bill(String amount, String categoryName, int categoryIcon, String payeeName, String notes,
      String dueDate, String nextDueDate,
      String repeatNumber, String billType, int flagRepeat,
      String remindNotification, int customDueDate) {
    this.amount = amount;
    this.categoryName = categoryName;
    this.categoryIcon = categoryIcon;
    this.payee = payeeName;
    this.notes = notes;
    this.customDueDate = customDueDate;
    this.dueDate = dueDate;
    this.nextDueDate = nextDueDate;
    this.repeat = flagRepeat;
    this.billType = billType;
    this.repeatEvery = repeatNumber;
    this.notification = remindNotification;
  }

  public Bill() {

  }

  public int getCustomDueDate() {
    return customDueDate;
  }

  public void setCustomDueDate(int customDueDate) {
    this.customDueDate = customDueDate;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public List<Bill> getChildBillList() {
    return childBillList;
  }

  public void setChildBillList(List<Bill> childBillList) {
    this.childBillList = childBillList;
  }

  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public int getCategoryIcon() {
    return categoryIcon;
  }

  public void setCategoryIcon(int categoryIcon) {
    this.categoryIcon = categoryIcon;
  }

  public String getPayee() {
    return payee;
  }

  public void setPayee(String payee) {
    this.payee = payee;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public int getRepeat() {
    return repeat;
  }

  public void setRepeat(int repeat) {
    this.repeat = repeat;
  }

  public String getRepeatEvery() {
    return repeatEvery;
  }

  public void setRepeatEvery(String repeatEvery) {
    this.repeatEvery = repeatEvery;
  }

  public String getNextDueDate() {
    return nextDueDate;
  }

  public void setNextDueDate(String nextDueDate) {
    this.nextDueDate = nextDueDate;
  }

  public String getNotification() {
    return notification;
  }

  public void setNotification(String notification) {
    this.notification = notification;
  }

  public RepeatBy getRepeatByType() {
    return repeatByType;
  }

  public void setRepeatByType(final RepeatBy repeatByType) {
    this.repeatByType = repeatByType;
  }

  public enum RepeatBy {
    DAY, WEEK, MONTH, YEAR;

    public static String getStringValue(RepeatBy repeatBy, Context context) {
      switch (repeatBy) {
        default:
        case DAY:
          return context.getString(R.string.day);
        case WEEK:
          return context.getString(R.string.week);
        case MONTH:
          return context.getString(R.string.month);
        case YEAR:
          return context.getString(R.string.year);
      }
    }

    public static RepeatBy getFromValue(final String value, Context context) {
      if (value.equals(context.getString(R.string.day))) {
        return DAY;
      } else if (value.equals(context.getString(R.string.week))) {
        return WEEK;
      } else if (value.equals(context.getString(R.string.month))) {
        return MONTH;
      } else if (value.equals(context.getString(R.string.year))) {
        return YEAR;
      }
      return null;
    }
  }

}
