package com.bankbabu.balance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Account;
import com.bankbabu.balance.models.Bill;
import com.bankbabu.balance.models.Bill.RepeatBy;
import com.bankbabu.balance.models.Category;
import com.bankbabu.balance.utils.DataContainer;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_NAME = "bill_reminder";

  private static final String CREATE_TABLE_ACCOUNT =
      "CREATE TABLE " + AccountEntry.TABLE_NAME + " ("
          + AccountEntry._ID + " INTEGER PRIMARY KEY autoincrement, "
          + AccountEntry.COLUMN_NAME + " TEXT, "
          + AccountEntry.COLUMN_NUMBER + " TEXT,"
          + AccountEntry.COLUMN_CODE + " TEXT,"
          + AccountEntry.COLUMN_BANK_NAME + " TEXT,"
          + AccountEntry.COLUMN_TYPE + " TEXT,"
          + AccountEntry.COLUMN_CONTACT_NUMBER + " TEXT,"
          + AccountEntry.COLUMN_NOTE + " TEXT,"
          + AccountEntry.COLUMN_OWNER_TYPE + " TEXT)";

  private static final String CREATE_TABLE_BILL = "CREATE TABLE " + BillEntry.TABLE_NAME + " ("
      + BillEntry._ID + " INTEGER PRIMARY KEY autoincrement, "
      + BillEntry.COLUMN_CAT_NAME + " TEXT, "
      + BillEntry.COLUMN_PAYEE_NAME + " TEXT, "
      + BillEntry.COLUMN_AMOUNT + " TEXT, "
      + BillEntry.COLUMN_NOTES + " TEXT, "
      + BillEntry.COLUMN_DUE_DATE + " TEXT, "
      + BillEntry.COLUMN_CUSTOM_DUE_DATE + " TEXT, "
      + BillEntry.COLUMN_REPEAT + " INTEGER, "
      + BillEntry.COLUMN_BILL_TYPE + " TEXT, "
      + BillEntry.COLUMN_REPEAT_EVERY + " TEXT, "
      + BillEntry.COLUMN_REPEAT_BY + " TEXT, "
      + BillEntry.COLUMN_NEXT_DUE_DATE + " TEXT, "
      + BillEntry.COLUMN_NOTIFICATION + " TEXT, "
      + BillEntry.COLUMN_STATUS + " INTEGER, "
      + BillEntry.COLUMN_CAT_ICON + " INTEGER )";

  private static final String CREATE_TABLE_CATEGORY =
      "CREATE TABLE " + CategoryEntry.TABLE_NAME + " ("
          + CategoryEntry._ID + " INTEGER PRIMARY KEY autoincrement, "
          + CategoryEntry.COLUMN_NAME + " TEXT, "
          + CategoryEntry.COLUMN_ICON + " INTEGER )";

  private static final String DROP_CONTACTS =
      "DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME;

  private static final String DROP_BILL =
      "DROP TABLE IF EXISTS " + BillEntry.TABLE_NAME;

  private static final String DROP_ACCOUNT =
      "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;

  private SQLiteDatabase db;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    db = this.getWritableDatabase();
  }

  public long insertCategory(Category contact) {
    final ContentValues values = categoryToContentValues(contact);
    return db.insert(CategoryEntry.TABLE_NAME, null, values);
  }

  @NonNull
  private ContentValues categoryToContentValues(final Category contact) {
    final ContentValues values = new ContentValues();
    values.put(CategoryEntry.COLUMN_NAME, contact.getName());
    values.put(CategoryEntry.COLUMN_ICON, contact.getIcon());
    return values;
  }

  @NonNull
  private ContentValues accountToContentValues(final Account contact) {
    ContentValues values = new ContentValues();
    values.put(AccountEntry.COLUMN_NAME, contact.getName());
    values.put(AccountEntry.COLUMN_NUMBER, contact.getNumber());
    values.put(AccountEntry.COLUMN_CODE, contact.getCode());
    values.put(AccountEntry.COLUMN_BANK_NAME, contact.getBankName());
    values.put(AccountEntry.COLUMN_TYPE, contact.getType());
    values.put(AccountEntry.COLUMN_CONTACT_NUMBER, contact.getContactNumber());
    values.put(AccountEntry.COLUMN_NOTE, contact.getNote());
    values.put(AccountEntry.COLUMN_OWNER_TYPE, contact.getOwnerType().getDbValue());
    return values;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_CATEGORY);
    db.execSQL(CREATE_TABLE_BILL);
    db.execSQL(CREATE_TABLE_ACCOUNT);

  }

  public void insertCategories(final Context context) {
    String[] categoryNames = context.getResources().getStringArray(R.array.category_names);
    for (int i = 0; i < categoryNames.length; i++) {
      final ContentValues values = categoryToContentValues(
          new Category(categoryNames[i], DataContainer.CATEGORY_ICONS[i]));
      db.insert(CategoryEntry.TABLE_NAME, null, values);
    }
  }

  public long insertAccount(Account contact) {
    final ContentValues values = accountToContentValues(contact);
    return db.insert(AccountEntry.TABLE_NAME, null, values);
  }


  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if(newVersion == 2 && oldVersion == 1) {
      db.execSQL("ALTER TABLE "
              + AccountEntry.TABLE_NAME + " ADD COLUMN "
              + AccountEntry.COLUMN_OWNER_TYPE +" TEXT");
    } else {
        db.execSQL(DROP_CONTACTS);
        db.execSQL(DROP_BILL);
        db.execSQL(DROP_ACCOUNT);
        onCreate(db);
    }
  }

  public int updateAccount(int id, Account contact) {
    ContentValues values = accountToContentValues(contact);
    return db.update(AccountEntry.TABLE_NAME, values, AccountEntry._ID + " = ?",
        new String[]{String.valueOf(id)});
  }

  public long insertBill(Bill bill) {
    final ContentValues values = billToContentValues(bill);
    return db.insert(BillEntry.TABLE_NAME, null, values);
  }

  @NonNull
  private ContentValues billToContentValues(final Bill bill) {
    final ContentValues values = new ContentValues();
    values.put(BillEntry.COLUMN_CAT_NAME, bill.getCategoryName());
    values.put(BillEntry.COLUMN_PAYEE_NAME, bill.getPayee());
    values.put(BillEntry.COLUMN_AMOUNT, bill.getAmount());
    values.put(BillEntry.COLUMN_NOTES, bill.getNotes());
    values.put(BillEntry.COLUMN_BILL_TYPE, bill.getBillType());
    values.put(BillEntry.COLUMN_DUE_DATE, bill.getDueDate());
    values.put(BillEntry.COLUMN_CUSTOM_DUE_DATE, bill.getCustomDueDate());
    values.put(BillEntry.COLUMN_REPEAT, bill.getRepeat());
    values.put(BillEntry.COLUMN_REPEAT_EVERY, bill.getRepeatEvery());
    values.put(BillEntry.COLUMN_REPEAT_BY, bill.getRepeatByType().toString());
    values.put(BillEntry.COLUMN_NEXT_DUE_DATE, bill.getNextDueDate());
    values.put(BillEntry.COLUMN_NOTIFICATION, bill.getNotification());
    values.put(BillEntry.COLUMN_STATUS, bill.getStatus());
    values.put(BillEntry.COLUMN_CAT_ICON, bill.getCategoryIcon());
    return values;
  }

  public int updateBill(int id, Bill bill) {
    ContentValues values = billToContentValues(bill);
    return db.update(BillEntry.TABLE_NAME, values, BillEntry._ID + " = ?",
        new String[]{String.valueOf(id)});
  }

  public int setPaidStatus(int billId) {
    final ContentValues values = new ContentValues();
    values.put(BillEntry.COLUMN_STATUS, 1);

    return db.update(BillEntry.TABLE_NAME, values, BillEntry._ID + " = ?",
        new String[]{String.valueOf(billId)});
  }

  public int deleteContact(int id) {
    return db.delete(CategoryEntry.TABLE_NAME, CategoryEntry._ID + " = ?",
        new String[]{String.valueOf(id)});
  }

  public int deleteBill(int id) {
    return db.delete(BillEntry.TABLE_NAME, BillEntry._ID + " = ?",
        new String[]{String.valueOf(id)});
  }

  public int deleteAccount(int id) {
    return db.delete(AccountEntry.TABLE_NAME, AccountEntry._ID + " = ?",
        new String[]{String.valueOf(id)});
  }

  public List<Category> getAllCategories() {
    try (final Cursor cursor = db.query(
        CategoryEntry.TABLE_NAME, null, null, null, null, null,
        CategoryEntry._ID + " DESC")) {
      final List<Category> categories = new ArrayList<>();

      while (cursor.moveToNext()) {
        categories.add(cursorToCategory(cursor));
      }

      return categories;
    }
  }

  private Category cursorToCategory(Cursor cursor) {
    final Category contact = new Category();
    contact.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(CategoryEntry._ID)));
    contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(CategoryEntry.COLUMN_NAME)));
    contact.setIcon(cursor.getInt(cursor.getColumnIndexOrThrow(CategoryEntry.COLUMN_ICON)));
    return contact;
  }

  public List<Bill> getBillsById(long id) {
    String query = "SELECT * FROM " + BillEntry.TABLE_NAME + " WHERE " + BillEntry._ID + " =" + id;
    try (Cursor cursor = db.rawQuery(query, null)) {
      final List<Bill> bills = new ArrayList<>();
      while (cursor.moveToNext()) {
        bills.add(cursorToBill(cursor, 0));
      }
      return bills;
    }
  }

  private Bill cursorToBill(Cursor cursor, int status) {
    final Bill bill = cursorToBill(cursor);
    bill.setChildBillList(getBillsBy(bill.getDueDate(), status));
    return bill;
  }

  private Bill cursorToBill(Cursor cursor) {
    Bill bill = new Bill();
    bill.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(BillEntry._ID)));
    bill.setCategoryName(
        cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_CAT_NAME)));
    bill.setPayee(cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_PAYEE_NAME)));
    bill.setAmount(cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_AMOUNT)));
    bill.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_NOTES)));
    bill.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_DUE_DATE)));
    bill.setRepeat(cursor.getInt(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_REPEAT)));
    bill.setCustomDueDate(
        cursor.getInt(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_CUSTOM_DUE_DATE)));
    bill.setBillType(cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_BILL_TYPE)));
    bill.setRepeatEvery(
        cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_REPEAT_EVERY)));
    bill.setRepeatByType(RepeatBy
        .valueOf(cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_REPEAT_BY))));
    bill.setNextDueDate(
        cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_NEXT_DUE_DATE)));
    bill.setNotification(
        cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_NOTIFICATION)));
    bill.setCategoryIcon(cursor.getInt(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_CAT_ICON)));
    bill.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_STATUS)));
    return bill;
  }

  private List<Bill> getBillsBy(String dueDate, int status) {
    try (Cursor cursor = db
        .query(BillEntry.TABLE_NAME, null, BillEntry.COLUMN_STATUS + " = ?",
            new String[]{String.valueOf(status)}, null, null, null)) {
      final List<Bill> bills = new ArrayList<>();

      while (cursor.moveToNext()) {
        if (cursor.getString(cursor.getColumnIndexOrThrow(BillEntry.COLUMN_DUE_DATE))
            .equals(dueDate)) {
          bills.add(cursorToBill(cursor));
        }
      }
      return bills;
    }
  }

  public List<Bill> getAllUnpaidBills() {
    try (Cursor cursor = db.query(BillEntry.TABLE_NAME, null,
        BillEntry.COLUMN_STATUS + " = '" + 0 + "'", null, BillEntry.COLUMN_DUE_DATE, null, null)) {

      final List<Bill> bills = new ArrayList<>();

      while (cursor.moveToNext()) {
        final Bill bill = cursorToBill(cursor, 0);
        if (bill != null) {
          bills.add(bill);
        }
      }

      return bills;
    }
  }

  public void updateBillDueDate(int billId, String date) {
    String updateQuery = "UPDATE " + BillEntry.TABLE_NAME + " SET "
        + BillEntry.COLUMN_DUE_DATE + " = '" + date + "' WHERE " + BillEntry._ID + " = " + billId;

    db.execSQL(updateQuery);
  }

  public List<Bill> getAllPaidBills() {
    try (Cursor cursor = db.query(BillEntry.TABLE_NAME, null,
        BillEntry.COLUMN_STATUS + " = '" + 1 + "'", null,
        BillEntry.COLUMN_DUE_DATE, null, null)) {
      final List<Bill> bills = new ArrayList<>();

      while (cursor.moveToNext()) {
        bills.add(cursorToBill(cursor, 1));
      }

      return bills;
    }
  }

  public int setBillUnPaid(int id) {
    ContentValues values = new ContentValues();
    values.put(BillEntry.COLUMN_STATUS, 0);

    return db.update(BillEntry.TABLE_NAME, values, BillEntry._ID + " = ?",
        new String[]{String.valueOf(id)});
  }

  public List<Account> getAllAccounts() {
    try (Cursor cursor = db.query(AccountEntry.TABLE_NAME, null, null,
        null, null, null, null)) {
      final List<Account> contactList = new ArrayList<>();

      while (cursor.moveToNext()) {
        contactList.add(cursorToAccount(cursor));
      }
      return contactList;
    }
  }

  private Account cursorToAccount(Cursor cursor) {
    final Account contact = new Account();
    contact.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AccountEntry._ID)));
    contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_NAME)));
    contact.setNumber(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_NUMBER)));
    contact.setCode(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_CODE)));
    contact
        .setBankName(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_BANK_NAME)));
    contact.setType(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_TYPE)));
    contact.setContactNumber(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.
        COLUMN_CONTACT_NUMBER)));
    contact.setNote(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_NOTE)));
    contact.setOwnerType(Account.OwnerType.fromDbValue(cursor.getString(cursor.getColumnIndexOrThrow(AccountEntry.COLUMN_OWNER_TYPE))));

    return contact;
  }

  private interface CategoryEntry extends BaseColumns {

    String TABLE_NAME = "category";

    String COLUMN_NAME = "name";
    String COLUMN_ICON = "icon";
  }

  private interface BillEntry extends BaseColumns {

    String TABLE_NAME = "bill";

    String COLUMN_CAT_NAME = "category_name";
    String COLUMN_CAT_ICON = "category_icon";
    String COLUMN_PAYEE_NAME = "payee";
    String COLUMN_AMOUNT = "amount";
    String COLUMN_NOTES = "notes";
    String COLUMN_DUE_DATE = "due_date";
    String COLUMN_CUSTOM_DUE_DATE = "custom_due_date";
    String COLUMN_BILL_TYPE = "bill_type";
    String COLUMN_REPEAT = "repeat";
    String COLUMN_REPEAT_EVERY = "repeat_every";
    String COLUMN_REPEAT_BY = "repeat_by";
    String COLUMN_NEXT_DUE_DATE = "next_due_date";
    String COLUMN_NOTIFICATION = "notification";
    String COLUMN_STATUS = "status";
  }

  private interface AccountEntry extends BaseColumns {

    String TABLE_NAME = "account";

    String COLUMN_NAME = "name";
    String COLUMN_NUMBER = "number";
    String COLUMN_CODE = "code";
    String COLUMN_BANK_NAME = "bank_name";
    String COLUMN_TYPE = "type";
    String COLUMN_CONTACT_NUMBER = "contact_number";
    String COLUMN_NOTE = "note";
    String COLUMN_OWNER_TYPE = "owner_type";
  }


}