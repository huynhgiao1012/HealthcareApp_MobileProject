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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<NewsArticle> newsArray;

    public NewsAdapter(Context context, ArrayList<NewsArticle> newsArray) {
        this.context = context;
        this.newsArray = newsArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView headerImage;
        private final TextView newsTitle, newsContent;
        private MaterialButton shareArticleBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headerImage = itemView.findViewById(R.id.newsHeaderImg);
            newsContent = itemView.findViewById(R.id.newsContent);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            shareArticleBtn = itemView.findViewById(R.id.shareArticleButton);

            shareArticleBtn.setOnClickListener(shareArticleHandler);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            NewsArticle currentNews = newsArray.get(getAdapterPosition());
            String newsURL = currentNews.getNewsURL();

            Intent intent = new Intent(context, WebView.class);
            intent.putExtra("newsURL", newsURL);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        private View.OnClickListener shareArticleHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsArticle currentNews = newsArray.get(getAdapterPosition());
                String newsURL = currentNews.getNewsURL();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, newsURL);
                context.startActivity(Intent.createChooser(intent, "Share To:"));
            }
        };
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        NewsArticle newsArticle = newsArray.get(position);
        holder.newsTitle.setText(newsArticle.getNewsTitle());
        holder.newsContent.setText(newsArticle.getNewsContent());

        // TODO: Replace with something that can load image more efficiently
        holder.headerImage.setImageResource(newsArticle.getHeaderImage());
    }

    @Override
    public int getItemCount() {
        return newsArray.size();
    }
}
