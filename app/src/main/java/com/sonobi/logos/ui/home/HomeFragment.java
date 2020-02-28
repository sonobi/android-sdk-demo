package com.sonobi.logos.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.sonobi.logos.R;
import com.sonobi.logos.SonobiMobileAds.DemandFetch;
import com.sonobi.logos.SonobiMobileAds.DemandFetchCallbackHandler;
import com.sonobi.logos.SonobiMobileAds.ExtraTrinityParams;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private PublisherAdView mPublisherAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        super.onCreate(savedInstanceState);


        mPublisherAdView = root.findViewById(R.id.publisherAdView);
        final PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();

        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                System.out.println("Ad Loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                System.out.println("Ad Failed to Loaded " + errorCode);

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        ExtraTrinityParams extraTrinityParamManager = new ExtraTrinityParams();
        String sizes = "";
        //Form our CSV of sizes
        for(AdSize adSize : mPublisherAdView.getAdSizes()) {
            sizes += adSize.getWidth() + "x" + adSize.getHeight() + ",";
        }

        DemandFetch SonobiDemandFetcher = new DemandFetch(sizes, "14360f54a3fbb014bbd2", extraTrinityParamManager, new DemandFetchCallbackHandler() {
            @Override
            public void onComplete(String resultCode) {
                System.out.println(adRequest);

                System.out.println(adRequest.build().getCustomTargeting());
                mPublisherAdView.loadAd(adRequest.build());

            }
        });
        SonobiDemandFetcher.setTestMode(true);
        SonobiDemandFetcher.setTimeout(15000);

        SonobiDemandFetcher.requestBid(adRequest);



        return root;

    }


}