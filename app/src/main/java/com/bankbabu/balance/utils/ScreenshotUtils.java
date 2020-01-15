package com.bankbabu.balance.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class ScreenshotUtils {

  public static File takeScreenshot(final Activity activity) {
    final Date now = new Date();
    DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

    try {
      final String path = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

      final View v1 = activity.getWindow().getDecorView().getRootView();
      v1.setDrawingCacheEnabled(true);
      final Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
      v1.setDrawingCacheEnabled(false);

      final File imageFile = new File(path);

      final FileOutputStream outputStream = new FileOutputStream(imageFile);
      final int quality = 100;

      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
      outputStream.flush();
      outputStream.close();

      return imageFile;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }
}
