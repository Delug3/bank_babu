package com.bankbabu.balance.adapters.blog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Articles;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Articles> dataArticles;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ArticlesAdapter(Context context, List<Articles>dataArticles) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.dataArticles= dataArticles;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blog, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Articles a = dataArticles.get(position);
        holder.titleTextView.setText(a.getTitle());
        holder.contentTextView.setText(a.getContent());

       /*
        Picasso.get()
                .load(content)
                .error(R.drawable.dollar)
                .into(holder.contentImageView);
        */

    }


    @Override
    public int getItemCount() {
        return dataArticles.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        TextView contentTextView;
        ImageView contentImageView;

        ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.text_title_article);
            contentTextView = itemView.findViewById(R.id.text_content_article);
            contentImageView = itemView.findViewById(R.id.image_view_article);

            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Articles getItem(int id) {
        return dataArticles.get(id);
    }


    public void setContext(Context context) {
        this.context = context;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}


