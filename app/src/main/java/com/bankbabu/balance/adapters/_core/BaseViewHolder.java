package com.bankbabu.balance.adapters._core;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public abstract class BaseViewHolder<T> extends ViewHolder implements OnClickListener {

  private T item;

  public BaseViewHolder(final View view) {
    super(view);

    initUI();
    setListener();
  }

  protected void initUI() {

  }

  protected void setListener() {

  }

  protected final <S extends View> S findViewById(@IdRes final int id) {
    return itemView.findViewById(id);
  }

  public void bind(final T item) {
    this.item = item;
  }

  @Override
  public void onClick(final View view) {

  }

  protected final T getItem() {
    return item;
  }
}
