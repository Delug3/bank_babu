package com.bankbabu.balance.adapters.welcome;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/31/19.
 */
public class WelcomeViewPagerAdapter extends PagerAdapter {

  private int[] layouts;

  public WelcomeViewPagerAdapter(final int[] layouts) {
    this.layouts = layouts;
  }

  @Override
  public int getCount() {
    return layouts.length;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    final LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());

    final View view = layoutInflater.inflate(layouts[position], container, false);
    container.addView(view);

    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    View view = (View) object;
    container.removeView(view);
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
    return view == obj;
  }
}

