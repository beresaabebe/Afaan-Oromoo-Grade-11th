package com.beckytech.afaanoromoograde11th.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.afaanoromoograde11th.BuildConfig;
import com.beckytech.afaanoromoograde11th.R;
import com.beckytech.afaanoromoograde11th.adapter.AboutAdapter;
import com.beckytech.afaanoromoograde11th.contents.AboutImages;
import com.beckytech.afaanoromoograde11th.contents.AboutName;
import com.beckytech.afaanoromoograde11th.contents.AboutUrlContents;
import com.beckytech.afaanoromoograde11th.model.AboutModel;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity implements AboutAdapter.OnLinkClicked {
    private InterstitialAd interstitialAd;
    private final String TAG = AboutActivity.class.getSimpleName();
    private AdView adView;
    private List<AboutModel> modelList;
    private final AboutImages images = new AboutImages();
    private final AboutName name = new AboutName();
    private final AboutUrlContents urlContents = new AboutUrlContents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        callAds();

        findViewById(R.id.ib_back).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        String str = "About us";
        TextView title = findViewById(R.id.tv_title);
        title.setText(str);

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/about.html");

        TextView version = findViewById(R.id.version_tv);
        version.setText(String.format(Locale.ENGLISH, " %s", BuildConfig.VERSION_NAME));

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(view -> {
            Toast.makeText(AboutActivity.this, "Share me, let other know about me!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            intent.putExtra(Intent.EXTRA_TEXT, R.string.app_name);
            startActivity(Intent.createChooser(intent, "Share me via "));
            showAdWithDelay();
        });

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
        showAdWithDelay();
    }

    private void callAds() {
        AudienceNetworkAds.initialize(this);

//        513372960928869_513374324262066
        adView = new AdView(this, "702081954795686_702083521462196", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();

        interstitialAd = new InterstitialAd(this, "702081954795686_702083634795518");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void showAdWithDelay() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Check if interstitialAd has been loaded successfully
            if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
                return;
            }
            // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
            if (interstitialAd.isAdInvalidated()) {
                return;
            }
            // Show the ad
            interstitialAd.show();
        }, 1000 * 60 * 2); // Show the ad after 15 minutes
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
}