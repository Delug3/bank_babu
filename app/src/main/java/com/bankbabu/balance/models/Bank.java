package com.bankbabu.balance.models;

import android.os.Parcel;
import com.bankbabu.balance.database.entity.History;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Bank {

  private int id;
  private String code;
  private int fav;
  private int gridView;
  private String name;
  private String shortName;
  private String inquiry;
  private String care;
  private String netBankApi;
  private String miniStatement;
  private boolean showShortName;

  public Bank() {
  }

  public Bank(final int id, final String name, final String inquiry, final String care,
      final String netBankApi,
      final String miniStatement) {
    this.id = id;
    this.name = name;
    this.inquiry = inquiry;
    this.care = care;
    this.netBankApi = netBankApi;
    this.miniStatement = miniStatement;
  }

  protected Bank(Parcel in) {
    id = in.readInt();
    code = in.readString();
    fav = in.readInt();
    gridView = in.readInt();
    name = in.readString();
    shortName = in.readString();
    inquiry = in.readString();
    care = in.readString();
    netBankApi = in.readString();
    miniStatement = in.readString();
    showShortName = in.readByte() != 0;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public int getFav() {
    return fav;
  }

  public void setFav(final int fav) {
    this.fav = fav;
  }

  public int getGridView() {
    return gridView;
  }

  public void setGridView(final int gridView) {
    this.gridView = gridView;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getInquiry() {
    return inquiry;
  }

  public void setInquiry(final String inquiry) {
    this.inquiry = inquiry;
  }

  public String getCare() {
    return care;
  }

  public void setCare(final String care) {
    this.care = care;
  }

  public String getNetBankApi() {
    return netBankApi;
  }

  public void setNetBankApi(final String netBankApi) {
    this.netBankApi = netBankApi;
  }

  public String getMiniStatement() {
    return miniStatement;
  }

  public void setMiniStatement(final String miniStatement) {
    this.miniStatement = miniStatement;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(final String shortName) {
    this.shortName = shortName;
  }

  public static Set<History> convertToHistory(Set<Bank> histories) {
    return new TreeSet<History>((o1, o2) -> {
      String value1 = o1.getName();
      String value2 = o2.getName();

      return value1.compareTo(value2);
    }) {
      {
        for (Bank bank :
            histories) {
          add(new History(bank.isShowShortName() ? bank.code : bank.name, bank.inquiry,
              bank.care,
              bank.netBankApi, bank.miniStatement));
        }
      }
    };
  }

  public boolean isShowShortName() {
    return showShortName;
  }

  public void setShowShortName(final boolean showShortName) {
    this.showShortName = showShortName;
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getCode(), getFav(), getGridView(), getName(), getShortName(), getInquiry(),
            getCare(), getNetBankApi(), getMiniStatement(), isShowShortName());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Bank)) {
      return false;
    }
    final Bank bank = (Bank) o;
    return getFav() == bank.getFav() &&
        getGridView() == bank.getGridView() &&
        isShowShortName() == bank.isShowShortName() &&
        Objects.equals(getCode(), bank.getCode()) &&
        Objects.equals(getName(), bank.getName()) &&
        Objects.equals(getShortName(), bank.getShortName()) &&
        Objects.equals(getInquiry(), bank.getInquiry()) &&
        Objects.equals(getCare(), bank.getCare()) &&
        Objects.equals(getNetBankApi(), bank.getNetBankApi()) &&
        Objects.equals(getMiniStatement(), bank.getMiniStatement());
  }
}
