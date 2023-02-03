package com.beckytech.afaanoromoograde11th;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.afaanoromoograde11th.activity.AboutActivity;
import com.beckytech.afaanoromoograde11th.activity.BookDetailActivity;
import com.beckytech.afaanoromoograde11th.adapter.Adapter;
import com.beckytech.afaanoromoograde11th.adapter.MoreAppsAdapter;
import com.beckytech.afaanoromoograde11th.contents.ContentEndPage;
import com.beckytech.afaanoromoograde11th.contents.ContentStartPage;
import com.beckytech.afaanoromoograde11th.contents.MoreAppImages;
import com.beckytech.afaanoromoograde11th.contents.MoreAppUrl;
import com.beckytech.afaanoromoograde11th.contents.MoreAppsName;
import com.beckytech.afaanoromoograde11th.contents.SubTitleContents;
import com.beckytech.afaanoromoograde11th.contents.TitleContents;
import com.beckytech.afaanoromoograde11th.model.Model;
import com.beckytech.afaanoromoograde11th.model.MoreAppsModel;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onBookClicked, MoreAppsAdapter.MoreAppsClicked {
    InterstitialAd interstitialAd;
    String TAG = MainActivity.class.getSimpleName();
    private final MoreAppImages images = new MoreAppImages();
    private final MoreAppUrl url = new MoreAppUrl();
    private final MoreAppsName appsName = new MoreAppsName();
    private List<MoreAppsModel> moreAppsModelList;
    private List<Model> modelList;
    private final TitleContents titleContents = new TitleContents();
    private final SubTitleContents subTitleContent = new SubTitleContents();
    private final ContentStartPage startPage = new ContentStartPage();
    private final ContentEndPage endPage = new ContentEndPage();    // Rate in app
    private ReviewInfo reviewInfo;
    private ReviewManager manager;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        AudienceNetworkAds.initialize(this);
        callAds();

        activateReviewInfo();

        AppRate.app_launched(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            MenuOptions(item);
            return true;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_main_item);
        getData();
        Adapter adapter = new Adapter(modelList, this);
        recyclerView.setAdapter(adapter);

        RecyclerView moreAppsRecyclerView = findViewById(R.id.moreAppsRecycler);
        getMoreApps();
        MoreAppsAdapter moreAppsAdapter = new MoreAppsAdapter(moreAppsModelList, this);
        moreAppsRecyclerView.setAdapter(moreAppsAdapter);

        if (reviewInfo != null) {
            Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
            flow.addOnCompleteListener(task -> {
                Menu menu_rate = navigationView.getMenu();
                menu_rate.findItem(R.id.rate_now).setVisible(false);
            });
        }
    }

    private void getMoreApps() {
        moreAppsModelList = new ArrayList<>();
        for (int i = 0; i < appsName.appNames.length; i++) {
            moreAppsModelList.add(new MoreAppsModel(appsName.appNames[i], url.url[i], images.images[i]));
        }
    }

    private void getData() {
        modelList = new ArrayList<>();
        for (int i = 0; i < titleContents.title.length; i++) {
            modelList.add(new Model(titleContents.title[i].substring(0, 1).toUpperCase() + "" + titleContents.title[i].substring(1).toLowerCase(),
                    subTitleContent.subTitle[i].substring(0, 1).toUpperCase() + "" + subTitleContent.subTitle[i].substring(1),
                    startPage.pageStart[i],
                    endPage.pageEnd[i]));
        }
    }

    @Override
    public void appClicked(MoreAppsModel model) {
        showAdWithDelay();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(model.getUrl()));
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void MenuOptions(MenuItem item) {
        if (item.getItemId() == R.id.action_about_us) {
            showAdWithDelay();
            startActivity(new Intent(this, AboutActivity.class));
        }

        if (item.getItemId() == R.id.action_rate) {
            String pkg = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
        }

        if (item.getItemId() == R.id.action_more_apps) {
            showAdWithDelay();
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/dev?id=6669279757479011928")));
        }

        if (item.getItemId() == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Download this app from Play store \n" + url);
            startActivity(Intent.createChooser(intent, "Choose to send"));
        }

        if (item.getItemId() == R.id.action_update) {
            showAdWithDelay();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            int lastVersion = pref.getInt("lastVersion", 0);
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            if (lastVersion < BuildConfig.VERSION_CODE) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                Toast.makeText(this, "New update is available download it from play store!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No update available!", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.action_exit) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Exit")
                    .setMessage("Do you want to close?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        System.exit(0);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setBackground(getResources().getDrawable(R.drawable.nav_header_bg, null))
                    .show();
        }
        if (item.getItemId() == R.id.rate_now) {
            startReviewFlow();
        }
    }

    @Override
    public void clickedBook(Model model) {
        showAdWithDelay();
        startActivity(new Intent(this, BookDetailActivity.class).putExtra("data", model));
    }

    private void callAds() {
//        513372960928869_513374324262066
        AdView adView = new AdView(this, "702081954795686_702084308128784", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();

        interstitialAd = new InterstitialAd(this, "702081954795686_702084514795430");
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

    void activateReviewInfo() {
        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> manaInfoTask = manager.requestReviewFlow();
        manaInfoTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else {
                Toast.makeText(this, "Review fail to start!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void startReviewFlow() {
        if (reviewInfo != null) {
            Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
            flow.addOnCompleteListener(task -> {
                Menu menu_rate = navigationView.getMenu();
                menu_rate.findItem(R.id.rate_now).setVisible(false);
                Toast.makeText(this, "Rating is complete!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}