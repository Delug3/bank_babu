package com.bankbabu.balance.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.bankbabu.balance.models.Bank;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/18/19.
 */
@Entity(tableName = "history")
public class History implements Parcelable {

  public static final Creator<History> CREATOR = new Creator<History>() {
    @Override
    public History createFromParcel(Parcel in) {
      return new History(in);
    }

    @Override
    public History[] newArray(int size) {
      return new History[size];
    }
  };
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private int id;
  @ColumnInfo(name = "name")
  private String name;
  @ColumnInfo(name = "inquiry")
  private String inquiry;
  @ColumnInfo(name = "care")
  private String care;
  @ColumnInfo(name = "netBankApi")
  private String netBankApi;
  @ColumnInfo(name = "miniStatement")
  private String miniStatement;

  public History() {
  }

  public History(final int id, final String name) {
    this.id = id;
    this.name = name;
  }

  public History(final String name, final String inquiry, final String care,
      final String netBankApi,
      final String miniStatement) {
    this.name = name;
    this.inquiry = inquiry;
    this.care = care;
    this.netBankApi = netBankApi;
    this.miniStatement = miniStatement;
  }

  protected History(Parcel in) {
    id = in.readInt();
    name = in.readString();
    inquiry = in.readString();
    care = in.readString();
    netBankApi = in.readString();
    miniStatement = in.readString();
  }

  public static Set<Bank> convertToBanks(List<History> histories) {
    return new TreeSet<Bank>((o1, o2) -> {
      String value1 = o1.isShowShortName() ? o1.getCode() : o1.getName();
      String value2 = o2.isShowShortName() ? o2.getCode() : o2.getName();

      return value1.compareTo(value2);
    }) {
      {
        for (History history :
            histories) {
          add(new Bank(history.id, history.name, history.inquiry, history.care, history.netBankApi,
              history.miniStatement));
        }
      }
    };
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(inquiry);
    dest.writeString(care);
    dest.writeString(netBankApi);
    dest.writeString(miniStatement);
  }

  @NonNull
  @Override
  public String toString() {
    return "History{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
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

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getInquiry(), getCare(), getNetBankApi(), getMiniStatement());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof History)) {
      return false;
    }
    final History history = (History) o;
    return Objects.equals(getName(), history.getName()) &&
        Objects.equals(getInquiry(), history.getInquiry()) &&
        Objects.equals(getCare(), history.getCare()) &&
        Objects.equals(getNetBankApi(), history.getNetBankApi()) &&
        Objects.equals(getMiniStatement(), history.getMiniStatement());
  }
}
