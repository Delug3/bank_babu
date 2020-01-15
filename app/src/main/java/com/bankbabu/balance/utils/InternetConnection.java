package com.bankbabu.balance.utils;

import android.content.Context;
import android.net.ConnectivityManager;


public class InternetConnection {

  public static boolean checkConnection(Context context) {
    final ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
      return connectivityManager.getActiveNetworkInfo() != null;
    } else {
      return false;
    }
  }
}
