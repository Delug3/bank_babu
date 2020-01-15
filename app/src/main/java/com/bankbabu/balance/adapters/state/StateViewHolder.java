package com.bankbabu.balance.adapters.state;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnStateFavoriteClickEvent;
import com.bankbabu.balance.events.OnStateItemClickEvent;
import com.bankbabu.balance.models.State;
import org.greenrobot.eventbus.EventBus;

public class StateViewHolder extends BaseViewHolder<State> {

  private ImageView imageViewBookmark;

  private TextView textViewTitle;

  StateViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void initUI() {
    imageViewBookmark = findViewById(R.id.image_view_bookmark);
    textViewTitle = findViewById(R.id.text_view_title);
  }

  @Override
  public void bind(final State state) {
    super.bind(state);

    textViewTitle.setText(state.getStateName());

    if (state.getGridView() == 0) {
      imageViewBookmark.setImageResource(R.drawable.ic_bookmark_0);
    } else {
      imageViewBookmark.setImageResource(R.drawable.ic_bookmark_1);
    }
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
    imageViewBookmark.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        EventBus.getDefault().post(new OnStateFavoriteClickEvent(getItem()));
      }
    });
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnStateItemClickEvent(getItem()));
  }
}
