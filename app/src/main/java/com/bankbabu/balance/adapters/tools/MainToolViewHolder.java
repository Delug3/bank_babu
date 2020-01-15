package com.bankbabu.balance.adapters.tools;

import android.view.View;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnMainToolItemClickEvent;
import com.bankbabu.balance.models.MainTool;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class MainToolViewHolder extends BaseViewHolder<MainTool> {

  private TextView textViewName;

  MainToolViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void initUI() {
    this.textViewName = itemView.findViewById(R.id.text_view_title);
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
  }

  @Override
  public void bind(final MainTool item) {
    super.bind(item);

    textViewName.setText(item.getName());
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnMainToolItemClickEvent(getItem().getIntentClass()));
  }
}
