package com.example.healthcareapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<NewsArticle> topNewsList;

    public TopNewsAdapter(Context context, ArrayList<NewsArticle> topNewsList) {
        this.context = context;
        this.topNewsList = topNewsList;
    }

    @NonNull
    @Override
    public TopNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_news_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsArticle currentNews = topNewsList.get(position);
        holder.newsTitle.setText(currentNews.getNewsTitle());

        holder.newsHeaderImg.setImageResource(currentNews.getHeaderImage());
    }

    @Override
    public int getItemCount() {
        return topNewsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView newsTitle;
        private final ImageView newsHeaderImg;
        private MaterialButton shareBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsHeaderImg = itemView.findViewById(R.id.newsHeaderImg);
            shareBtn = itemView.findViewById(R.id.shareButton);

            shareBtn.setOnClickListener(shareArticleHandler);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            NewsArticle currentNews = topNewsList.get(getAdapterPosition());
            String newsURL = currentNews.getNewsURL();

            Intent intent = new Intent(context, WebView.class);
            intent.putExtra("newsURL", newsURL);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        private View.OnClickListener shareArticleHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsArticle currentNews = topNewsList.get(getAdapterPosition());
                String newsURL = currentNews.getNewsURL();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, newsURL);
                context.startActivity(Intent.createChooser(intent, "Share To:"));
            }
        };
    }
}
