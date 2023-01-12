package com.beckytech.afaanoromoograde11th.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.afaanoromoograde11th.BuildConfig;
import com.beckytech.afaanoromoograde11th.R;
import com.beckytech.afaanoromoograde11th.adapter.AboutAdapter;
import com.beckytech.afaanoromoograde11th.contents.AboutImages;
import com.beckytech.afaanoromoograde11th.contents.AboutName;
import com.beckytech.afaanoromoograde11th.contents.AboutUrlContents;
import com.beckytech.afaanoromoograde11th.model.AboutModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity implements AboutAdapter.OnLinkClicked {
    private List<AboutModel> modelList;
    private final AboutImages images = new AboutImages();
    private final AboutName name = new AboutName();
    private final AboutUrlContents urlContents = new AboutUrlContents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        findViewById(R.id.ib_back).setOnClickListener(v -> onBackPressed());
        String str = "About us";
        TextView title = findViewById(R.id.tv_title);
        title.setText(str);

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/about.html");

        TextView version = findViewById(R.id.version_tv);
        version.setText(String.format(Locale.ENGLISH, " %s", BuildConfig.VERSION_NAME));

        RecyclerView recyclerView = findViewById(R.id.recycler_about);
        getData();
        AboutAdapter adapter = new AboutAdapter(modelList, this);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        modelList = new ArrayList<>();
        for (int i = 0; i < name.name.length; i++) {
            modelList.add(new AboutModel(images.images[i],
                    name.name[i], urlContents.url[i]));
        }
    }

    @Override
    public void linkClicked(AboutModel model) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(model.getUrl()));
        startActivity(intent);
    }
}