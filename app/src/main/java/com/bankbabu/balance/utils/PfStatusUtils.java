package com.bankbabu.balance.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import com.bankbabu.balance.R;


public class PfStatusUtils {

  public static Intent call(final Object phoneNumber) {
    return new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel:%s", phoneNumber)));
  }

  public static Intent sms(final String phoneNumber, final String text) {
    return new Intent(Intent.ACTION_SENDTO, Uri.parse(String.format("smsto:%s", phoneNumber)))
        .putExtra("sms_body", text);
  }

  public static Intent webpage(final String url) {
    return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
  }

  private static void safeStartActivity(final Intent intent, final Context context) {
    PackageManager packageManager = context.getPackageManager();
    if (intent.resolveActivity(packageManager) != null) {
      context.startActivity(intent);
    } else {
      Toast.makeText(context, R.string.intent_cannot_resolve, Toast.LENGTH_SHORT).show();
    }
  }
}
