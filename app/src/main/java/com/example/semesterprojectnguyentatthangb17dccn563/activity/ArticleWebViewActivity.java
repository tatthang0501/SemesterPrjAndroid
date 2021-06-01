package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.semesterprojectnguyentatthangb17dccn563.R;


public class ArticleWebViewActivity extends AppCompatActivity {
    private WebView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_web_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        view = findViewById(R.id.webviewArticle);

        Intent i = getIntent();
        String url = (String) i.getSerializableExtra("url");
        view.loadUrl(url);
    }
}