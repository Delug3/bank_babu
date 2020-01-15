package com.bankbabu.balance.adapters.carousel;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.bankbabu.balance.R;
import com.rd.PageIndicatorView;

import java.util.List;

public class CarouselView extends ConstraintLayout {
    private static final int LAYOUT = R.layout.view_card_carousel;

    @Nullable
    private CarouselAdapter.OnCarouselItemClicked cardClickListener;

    private RecyclerView cardScrollView;
    private PageIndicatorView pageIndicatorView;
    private CarouselAdapter cardsAdapter;
    private int selectedCardIndex;

    public CarouselView(Context context) {
        super(context);
        inflate(getContext(), LAYOUT, this);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), LAYOUT, this);

    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), LAYOUT, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        cardScrollView = findViewById(R.id.cardList);
        pageIndicatorView = findViewById(R.id.cardIndicatorView);

        setupViews();
    }

    private void setupViews() {
        cardScrollView.setLayoutManager(new CenterZoomLayoutManager(getContext()));

        SnapHelper helper = new CarouselSnapHelper();
        helper.attachToRecyclerView(cardScrollView);

        cardsAdapter = new CarouselAdapter(this::onCardClicked);
        cardScrollView.setAdapter(cardsAdapter);

        int sidePaddingInDp = getSidePadding();
        cardScrollView.setPadding(sidePaddingInDp, 0, sidePaddingInDp, 0);

        cardScrollView.post(() -> onSnapToItem(selectedCardIndex));
    }

    private void onCardClicked(int position) {
        selectCardByIndex(position);
        onSnapToItem(position);
        if (cardClickListener != null) {
            cardClickListener.onCarouselItemClicked(position);
        }
    }

    /**
     * Returns the padding that should be used around the items of the carousel.
     * <p>
     * This padding is used to center on items.
     *
     * @return padding to be set for the scroll view.
     */
    private @Px
    int getSidePadding() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int totalPaddingPixels = displayMetrics.widthPixels - getCardItemWidth() - (getCardMargin() * 2);
        return totalPaddingPixels / 2;
    }

    private void onSnapToItem(int itemPosition) {
        selectedCardIndex = itemPosition;
        pageIndicatorView.setSelected(selectedCardIndex);
    }

    public void selectCardByIndex(int selectedCardIndex) {
        this.selectedCardIndex = selectedCardIndex;
        post(() -> {
            this.pageIndicatorView.setSelected(selectedCardIndex);
            this.cardScrollView.smoothScrollToPosition(selectedCardIndex);
        });
    }

    private @Px
    int getCardItemWidth() {
        return getResources().getDimensionPixelSize(R.dimen.carousel_card_width);
    }

    private @Px
    int getCardMargin() {
        return getResources().getDimensionPixelSize(R.dimen.carousel_card_margin);
    }

    public void setCardClickListener(@Nullable CarouselAdapter.OnCarouselItemClicked cardClickListener) {
        this.cardClickListener = cardClickListener;
    }

    public void setItems(List<CarouselItem> items) {
        cardsAdapter.setItems(items);
        pageIndicatorView.setCount(items.size());
    }

    public void notifyDataSetChanged() {
        cardsAdapter.notifyDataSetChanged();
    }

    private class CarouselSnapHelper extends PagerSnapHelper {
        @Nullable
        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                                  @NonNull View targetView) {
            int[] distance = super.calculateDistanceToFinalSnap(layoutManager, targetView);

            int itemPosition = layoutManager.getPosition(targetView);
            onSnapToItem(itemPosition);

            return distance;
        }
    }
}
