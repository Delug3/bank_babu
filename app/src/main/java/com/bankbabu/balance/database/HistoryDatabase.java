package com.bankbabu.balance.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.bankbabu.balance.database.dao.HistoryDao;
import com.bankbabu.balance.database.entity.History;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/18/19.
 */
@Database(entities = {History.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {

  private static volatile HistoryDatabase INSTANCE;

  public static HistoryDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (HistoryDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              HistoryDatabase.class, "history_database")
              .allowMainThreadQueries()
              .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract HistoryDao getHistoryDao();
}
