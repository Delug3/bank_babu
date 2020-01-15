package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.utils.SharedPreferenceUtils;
import com.bankbabu.balance.views.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {

  @BindView(R.id.view_pager) ViewPager viewPager;
  @BindView(R.id.wrapper_dots) LinearLayout wrapperDots;
  @BindView(R.id.button_skip) Button buttonSkip;
  @BindView(R.id.button_next) Button buttonNext;
  private int[] layouts;



  @Override
  protected int getContentView() {
    return R.layout.activity_welcome;
  }

  @Override
  protected void initUi() {
    ButterKnife.bind(this);
  }

  @Override
  protected void setUi(final Bundle savedInstanceState) {
    logEvent("welcome");
//    if (!SharedPreferenceUtils.getInstance().isFirstTimeLaunch()) {
    launchHomeScreen();
    finish();
//    }
//    showLanguageDialog();
//
//    new DatabaseHelper(this).insertCategories(this);
//
//    layouts = new int[]{
//        R.layout.welcome_slide_1,
//        R.layout.welcome_slide_2,
//        R.layout.welcome_slide_3,
//        R.layout.welcome_slide_4};
//
//    addBottomDots(0);
//
//    final PagerAdapter pagerAdapter = new WelcomeViewPagerAdapter(layouts);
//    viewPager.setAdapter(pagerAdapter);
//    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//      @Override
//      public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//      }
//
//      @Override
//      public void onPageSelected(int position) {
//        addBottomDots(position);
//
//        // changing the next button text 'NEXT' / 'GOT IT'
//        if (position == layouts.length - 1) {
//          // last page. make button text to GOT IT
//          buttonNext.setText(getString(R.string.start));
//          buttonSkip.setVisibility(View.GONE);
//        } else {
//          // still pages are left
//          buttonNext.setText(getString(R.string.next));
//          buttonSkip.setVisibility(View.VISIBLE);
//        }
//      }
//
//      @Override
//      public void onPageScrollStateChanged(int arg0) {
//
//      }
//    });
//
//    buttonSkip.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        launchDashboardScreen();
//      }
//    });
//
//    buttonNext.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        // checking for last page
//        // if last page home screen will be launched
//        int current = getItem();
//        if (current < layouts.length) {
//          // move to next screen
//          viewPager.setCurrentItem(current);
//        } else {
//          launchHomeScreen();
//        }
//      }
//    });
  }

  private void launchHomeScreen() {
    SharedPreferenceUtils.getInstance().setFirstTimeLaunch(false);
    startActivity(new Intent(WelcomeActivity.this, ToolsActivity.class));
    finish();
  }

  private void addBottomDots(int currentPage) {
    final TextView[] dots = new TextView[layouts.length];

    int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
    int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

    wrapperDots.removeAllViews();
    for (int i = 0; i < dots.length; i++) {
      dots[i] = new TextView(this);
      dots[i].setText(Html.fromHtml("&#8226;"));
      dots[i].setTextSize(35);
      dots[i].setTextColor(colorsInactive[currentPage]);
      wrapperDots.addView(dots[i]);
    }

    if (dots.length > 0) {
      dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
  }

  private void launchDashboardScreen() {
    startActivity(new Intent(WelcomeActivity.this, ToolsActivity.class));
    finish();
  }

  private int getItem() {
    return viewPager.getCurrentItem() + 1;
  }
}
