package com.beckytech.afaanoromoograde11th.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beckytech.afaanoromoograde11th.R;
import com.beckytech.afaanoromoograde11th.model.Model;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;
    private final String TAG = BookDetailActivity.class.getSimpleName();
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        callAds();

        findViewById(R.id.back_book_detail).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        Intent intent = getIntent();
        Model model = (Model) intent.getSerializableExtra("data");

        TextView title = findViewById(R.id.title_book_detail);
        title.setSelected(true);
        assert model != null;
        title.setText(model.getTitle());

        TextView subTitle = findViewById(R.id.sub_title_book_detail);
        subTitle.setSelected(true);
        subTitle.setText(model.getSubTitle());

        PDFView pdfView = findViewById(R.id.pdfView);

        int start = model.getStartPage();
        int end = model.getEndPage();

        List<Integer> list = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            list.add(i);
        }

        int[] array = new int[list.size()];

        for (int j = 1; j < array.length; j++) {
            array[j] = list.get(j);
        }

        pdfView.fromAsset("ao11.pdf")
                .pages(array)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .spacing(10)
                .enableDoubletap(true)
                .fitEachPage(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void callAds() {
        AudienceNetworkAds.initialize(this);
//        513372960928869_513374324262066
        adView = new AdView(this, "702081954795686_702084044795477", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();

        interstitialAd = new InterstitialAd(this, "702081954795686_702083901462158");
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