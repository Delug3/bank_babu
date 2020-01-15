package com.bankbabu.balance.adapters.carousel;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CenterZoomLayoutManager extends LinearLayoutManager {
    /**
     * Defines how much a view is shrinked (percentage)
     */
    private final float mShrinkAmount = 0.15f;
    /**
     * Defines how long do we need to scroll from scroll view center for item to start shrinking.
     * <p>
     * Given 0.9, the shrinking starts after we've scrolled 10% of the item width (1.f - 0.9f).
     */
    private final float mShrinkDistance = 0.9f;

    CenterZoomLayoutManager(Context context) {
        this(context, HORIZONTAL, false);
    }

    private CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        scaleChildren();
    }

    @Override public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            float midpoint = getHeight() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleChildren();
            return scrolled;
        } else {
            return 0;
        }
    }

    private void scaleChildren() {
        float midpoint = getWidth() / 2.f;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float scale = calculateScale(midpoint, child);

            child.setPivotX(child.getWidth() / 2);
            child.setPivotY(child.getHeight() /2);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

    private float calculateScale(float midPoint, View child) {
        float d1 = mShrinkDistance * midPoint;
        return 1f - mShrinkAmount * Math.min(
                d1,
                Math.abs(midPoint - (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f)
        ) / d1;
    }
}