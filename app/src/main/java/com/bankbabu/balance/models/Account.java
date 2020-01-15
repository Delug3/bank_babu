package com.bankbabu.balance.models;

import android.content.Context;

import com.bankbabu.balance.R;

import java.io.Serializable;

/**
 * Created by Nainesh on 16-04-2018.
 */

public class Account implements Serializable {

    private int id;
    private String name, number;
    private String code;
    private String bankName;
    private String type;
    private String contactNumber;
    private String note;
    private OwnerType ownerType;

    public Account(String name, String number, String code, String bankName, String type, String cno,
                   String note, OwnerType ownerType) {
        this.name = name;
        this.number = number;
        this.code = code;
        this.bankName = bankName;
        this.type = type;
        this.contactNumber = cno;
        this.note = note;
        this.ownerType = ownerType;
    }

    public Account() {

    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShareString(Context context) {
        return context.getString(getOwnerType() == OwnerType.ACCOUNT
                        ? R.string.account_share_string
                        : R.string.contact_share_string,
                getName(), getNumber(), getBankName());
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public enum OwnerType {
        ACCOUNT("account"),
        CONTACT("contact"),
        UNKNOWN("unknown");

        private final String dbValue;

        OwnerType(String dbValue) {
            this.dbValue = dbValue;
        }

        public static OwnerType fromDbValue(String dbValue) {
            for (OwnerType type : OwnerType.values()) {
                if (type.dbValue.equalsIgnoreCase(dbValue)) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public String getDbValue() {
            return dbValue;
        }


    }
}
