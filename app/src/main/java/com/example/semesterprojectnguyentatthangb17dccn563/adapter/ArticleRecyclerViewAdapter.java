package com.example.semesterprojectnguyentatthangb17dccn563.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.activity.ArticleWebViewActivity;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ArticleViewHolder>{
    private List<Article> mList;
    private Context context;

    public ArticleRecyclerViewAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    public void setListArticle(List<Article> list){
        this.mList = list;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article t = mList.get(position);
        if(t != null){
            holder.tvName.setText(t.getTitle());
            Picasso.get().load(t.getImgUrl()).placeholder(R.color.design_default_color_background).into(holder.imgArticle);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ArticleWebViewActivity.class);
                    i.putExtra("url", t.getUrl());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgArticle;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameArticle);
            imgArticle = itemView.findViewById(R.id.imgArticle);
        }
    }
}
