package com.bankbabu.balance.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

  private final int spacing;
  private final int spanCount;

  public GridSpacingItemDecoration(int spacing, final int spanCount) {
    this.spacing = spacing;
    this.spanCount = spanCount;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    if (spanCount <= 2) {
      final int position = parent.getChildAdapterPosition(view);
      int col = position % spanCount;
      if (col == 0) {
        outRect.left = spacing;
      } else if (col == 1) {
        outRect.right = spacing;
      }
    }
  }
}
