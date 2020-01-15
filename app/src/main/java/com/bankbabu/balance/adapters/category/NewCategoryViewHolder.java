package com.bankbabu.balance.adapters.category;

import android.view.View;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnNewCategoryItemClickEvent;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class NewCategoryViewHolder extends BaseViewHolder<Integer> {

  NewCategoryViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnNewCategoryItemClickEvent(getItem()));
  }
}
