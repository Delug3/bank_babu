package com.bankbabu.balance.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 4/8/19.
 */
public class ViewPager extends android.support.v4.view.ViewPager {

  public ViewPager(@NonNull final Context context) {
    super(context);
  }

  public ViewPager(@NonNull final Context context,
      @Nullable final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    try {
      return super.onInterceptTouchEvent(ev);
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
    return false;
  }

}
