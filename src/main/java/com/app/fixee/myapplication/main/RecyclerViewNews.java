package com.app.fixee.myapplication.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseClickNews;
import com.app.fixee.myapplication.models.News;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewNews extends RecyclerView.Adapter<RecyclerViewNews.NewsViewHolder> {

    private static final String notPost = "Не опубликовано";
    private View view;

    private List<News> news;
    private int accessLevel;
    private OnResponseClickNews callback;

    RecyclerViewNews(List<News> news, int accessLevel, OnResponseClickNews callback) {
        this.news = news;
        this.accessLevel = accessLevel;
        this.callback = callback;
    }

    @Override
    public RecyclerViewNews.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewNews.NewsViewHolder holder, int position) {
        holder.title.setText(news.get(position).getTitle());
        holder.description.setText(news.get(position).getDescription());
        holder.date.setText(news.get(position).getDate());
        holder.author.setText(news.get(position).getAuthor());

        if (!news.get(position).getPhoto().isEmpty()) {

            holder.imageView.setVisibility(View.VISIBLE);

            Glide.with(view)
                    .load(news.get(position).getPhoto())
                    .into(holder.imageView);
        }

        if (news.get(position).getStatus() == 1 && accessLevel == 3) {

            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText(notPost);

            holder.linearLayout.setOnClickListener(view -> callback.onSuccess(news.get(position).getDocId(), position));
        }
    }

    @Override
    public int getItemCount() {
        return news.isEmpty() ? 0 : news.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView title;
        TextView description;
        TextView date;
        TextView author;
        TextView status;
        ImageView imageView;

        public NewsViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            author = itemView.findViewById(R.id.author);
            status = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
