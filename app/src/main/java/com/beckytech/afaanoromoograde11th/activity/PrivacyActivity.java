package com.beckytech.afaanoromoograde11th.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beckytech.afaanoromoograde11th.R;

public class PrivacyActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(view -> onBackPressed());
        ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setVisibility(View.GONE);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.privacy_title);

        WebView activity_privacy = findViewById(R.id.webView_privacy);
        activity_privacy.loadUrl("https://yoosaad.com/beresa-android-website-privacy-policy/");
        activity_privacy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        activity_privacy.getSettings().getLoadsImagesAutomatically();
        activity_privacy.getSettings().setJavaScriptEnabled(true);
        activity_privacy.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                activity_privacy.loadUrl("file:///android_asset/error.html");
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}