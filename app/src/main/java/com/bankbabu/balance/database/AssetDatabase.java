package com.bankbabu.balance.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import com.bankbabu.balance.models.Bank;
import com.bankbabu.balance.models.Holiday;
import com.bankbabu.balance.models.SmsBank;
import com.bankbabu.balance.models.State;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.util.ArrayList;
import java.util.List;

public class AssetDatabase extends SQLiteAssetHelper {

  private static final String DATABASE_NAME = "google_crystalics.db";
  private static final int DATABASE_VERSION = 2;

  public AssetDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public List<Bank> getBanks() {
    final SQLiteDatabase db = getWritableDatabase();

    try (final Cursor cursor = db.query(BankEntry.TABLE_NAME, null, null, null,
        null, null, BankEntry.BANK_FAV + " DESC")) {
      final List<Bank> banks = new ArrayList<>();

      while (cursor.moveToNext()) {
        banks.add(cursorToBank(cursor));
      }

      return banks;
    }
  }

  private Bank cursorToBank(final Cursor cursor) {
    final Bank bank = new Bank();
    bank.setId(cursor.getInt(cursor.getColumnIndex(BankEntry.BANK_ID)));
    bank.setName(cursor.getString(cursor.getColumnIndex(BankEntry.BANK_NAME)));
    bank.setCare(cursor.getString(cursor.getColumnIndex(BankEntry.BANK_CARE)));
    bank.setInquiry(cursor.getString(cursor.getColumnIndex(BankEntry.BANK_INQUIRY)));
    bank.setNetBankApi(cursor.getString(cursor.getColumnIndex(BankEntry.BANK_NET)));
    bank.setCode(cursor.getString(cursor.getColumnIndex(BankEntry.BANK_CODE)));
    bank.setFav(cursor.getInt(cursor.getColumnIndex(BankEntry.BANK_FAV)));
    bank.setMiniStatement(cursor.getString(cursor.getColumnIndex(BankEntry.BANK_STATEMENT)));
    return bank;
  }

  public List<Bank> getBanksWithGridView() {
    final SQLiteDatabase db = getWritableDatabase();

    try (final Cursor cursor = db
        .query(BankEntry.TABLE_NAME, null, BankEntry.BANK_GRID_VIEW + " = ?", new String[]{"1"},
            null, null, BankEntry.BANK_FAV + " DESC")) {
      final List<Bank> banks = new ArrayList<>();

      while (cursor.moveToNext()) {
        banks.add(cursorToBank(cursor));
      }

      return banks;
    }
  }

  public int setBankFavorite(int bankId, boolean isFavorite) {
    final String updateQuery = "UPDATE " + BankEntry.TABLE_NAME + " SET "
        + BankEntry.BANK_FAV + " = " + (isFavorite ? 1 : 0) +
        " WHERE " + BankEntry.BANK_ID + " = " + bankId;

    final SQLiteDatabase db = getWritableDatabase();
    final SQLiteStatement statement = db.compileStatement(updateQuery);

    return statement.executeUpdateDelete();
  }

  public List<Bank> getFavoriteBanks() {
    final SQLiteDatabase db = getWritableDatabase();

    try (final Cursor cursor = db
        .query(BankEntry.TABLE_NAME, null, BankEntry.BANK_FAV + " = ?", new String[]{"1"},
            null, null, BankEntry.BANK_FAV + " DESC")) {
      final List<Bank> banks = new ArrayList<>();

      while (cursor.moveToNext()) {
        banks.add(cursorToBank(cursor));
      }

      return banks;
    }
  }

  public List<SmsBank> getSmsBanksByName(@NonNull final String bankName) {
    final SQLiteDatabase db = getWritableDatabase();

    try (final Cursor cursor = db.query(SmsEntry.TABLE_NAME, null,
        SmsEntry.BANK_BANK_NAME + " = ? ", new String[]{bankName}, null, null, null)) {
      final List<SmsBank> banks = new ArrayList<>();

      while (cursor.moveToNext()) {
        banks.add(cursorToSmsBank(cursor));
      }

      return banks;
    }
  }

  @NonNull
  private SmsBank cursorToSmsBank(@NonNull final Cursor cursor) {
    final SmsBank smsBank = new SmsBank();
    smsBank.setId(cursor.getInt(cursor.getColumnIndex(SmsEntry.SMS_ID)));
    smsBank.setTitle(cursor.getString(cursor.getColumnIndex(SmsEntry.BANK_TITLE)));
    smsBank.setBankName(cursor.getString(cursor.getColumnIndex(SmsEntry.BANK_BANK_NAME)));
    smsBank.setMessage(cursor.getString(cursor.getColumnIndex(SmsEntry.BANK_MASSAGE)));
    smsBank.setInfo(cursor.getString(cursor.getColumnIndex(SmsEntry.BANK_INFO)));
    smsBank.setPhoneNumber(cursor.getString(cursor.getColumnIndex(SmsEntry.BANK_PHONE)));
    return smsBank;
  }

  public List<State> getStates() {
    final SQLiteDatabase db = getWritableDatabase();

    try (final Cursor cursor = db.query(StateEntry.TABLE_NAME, null, null, null,
        null, null, StateEntry.STATE_GRID + " DESC")) {
      final List<State> states = new ArrayList<>();

      while (cursor.moveToNext()) {
        states.add(cursorToState(cursor));
      }

      return states;
    }
  }

  @NonNull
  private State cursorToState(final Cursor cursor) {
    State state = new State();
    state.setStateId(cursor.getInt(cursor.getColumnIndex(StateEntry.STATE_ID)));
    state.setStateName(cursor.getString(cursor.getColumnIndex(StateEntry.STATE_NAME)));
    state.setGridView(cursor.getInt(cursor.getColumnIndex(StateEntry.STATE_GRID)));
    return state;
  }

  public List<Holiday> getHolidaysByStateId(int stateId) {
    final SQLiteDatabase db = getWritableDatabase();

    try (final Cursor cursor = db.query(HolidayEntry.TABLE_NAME, null,
        HolidayEntry.STATE_ID + " = ?", new String[]{String.valueOf(stateId)},
        null, null, null)) {
      final List<Holiday> holidays = new ArrayList<>();

      while (cursor.moveToNext()) {
        holidays.add(cursorToHoliday(cursor));
      }

      return holidays;
    }
  }

  @NonNull
  private Holiday cursorToHoliday(final Cursor cursor) {
    Holiday questions = new Holiday();
    questions.setHolidayId(cursor.getInt(cursor.getColumnIndex(HolidayEntry.HOLIDAY_ID)));
    questions.setHolidayName(cursor.getString(cursor.getColumnIndex(HolidayEntry.HOLIDAY_NAME)));
    questions.setHolidayDate(cursor.getString(cursor.getColumnIndex(HolidayEntry.HOLIDAY_DATE)));
    return questions;
  }

  public int setStateFavorite(int stateId, boolean isFavorite) {
    final String updateQuery = "UPDATE " + StateEntry.TABLE_NAME + " SET "
        + StateEntry.STATE_GRID + " = " + (isFavorite ? 1 : 0) +
        " WHERE " + StateEntry.STATE_ID + " = " + stateId;

    final SQLiteDatabase db = getWritableDatabase();
    final SQLiteStatement statement = db.compileStatement(updateQuery);

    return statement.executeUpdateDelete();
  }

  private interface BankEntry {

    String TABLE_NAME = "tbl_bank";

    String BANK_ID = "bank_id";
    String BANK_NAME = "bank_name";
    String BANK_SHORT_NAME = "short_name";
    String BANK_INQUIRY = "bank_inquiry";
    String BANK_CARE = "bank_care";
    String BANK_CODE = "code";
    String BANK_FAV = "bank_fav";
    String BANK_NET = "netbank_api";
    String BANK_STATEMENT = "mini_statement";
    String BANK_GRID_VIEW = "grid_view";
  }

  private interface SmsEntry {

    String TABLE_NAME = "smsinfo";

    String SMS_ID = "id";
    String BANK_TITLE = "title";
    String BANK_BANK_NAME = "bankname";
    String BANK_MASSAGE = "message";
    String BANK_INFO = "info";
    String BANK_PHONE = "phno";
  }

  private interface StateEntry {

    String TABLE_NAME = "state";

    String STATE_ID = "id";
    String STATE_NAME = "state_name";
    String STATE_GRID = "grid_view";
  }

  private interface HolidayEntry {

    String TABLE_NAME = "holiday";

    String HOLIDAY_ID = "id";
    String HOLIDAY_NAME = "title";
    String HOLIDAY_DATE = "date";
    String STATE_ID = "state_id";
  }

}