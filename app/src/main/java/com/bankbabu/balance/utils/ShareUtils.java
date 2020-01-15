package com.bankbabu.balance.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.widget.Toast;
import com.bankbabu.balance.R;
import java.io.File;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public class ShareUtils {

  public static void shareScreen(@NonNull File file, @NonNull Context context,
      final String shareString) {
    final Uri uri = FileProvider.getUriForFile(context,
        context.getPackageName() + ".provider", file);
    final Intent sharingIntent = new Intent(Intent.ACTION_SEND)
        .setType("image/*");
    String shareBody = shareString + "\n https://play.google.com/store/apps/details?id=" + context
        .getPackageName();
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
        .putExtra(Intent.EXTRA_TEXT, shareBody)
        .putExtra(Intent.EXTRA_STREAM, uri);

    safeStartActivity(Intent.createChooser(sharingIntent, "Share via"), context);
  }

  public static void shareString(@NonNull Context context,
                                 final String shareString) {
    final Intent sharingIntent = new Intent(Intent.ACTION_SEND)
          .setType("text/*");
    String shareBody = shareString + "\n https://play.google.com/store/apps/details?id=" + context
            .getPackageName();
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            .putExtra(Intent.EXTRA_TEXT, shareBody);

    safeStartActivity(Intent.createChooser(sharingIntent, "Share via"), context);
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
