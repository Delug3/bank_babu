package com.bankbabu.balance.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.bankbabu.balance.database.entity.History;
import java.util.List;
import java.util.Set;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/18/19.
 */
@Dao
public interface HistoryDao {

  @Insert
  void insert(History history);

  @Insert
  void insert(Set<History> histories);

  @Query("DELETE FROM history")
  void deleteAll();

  @Query("SELECT * from history ORDER BY name ASC")
  List<History> getAllHistory();
}
