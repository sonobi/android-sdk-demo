package com.sonobi.logos.ui.interstitial;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.sonobi.logos.R;
import com.sonobi.logos.SonobiMobileAds.DfpInterstitialAd;
import com.sonobi.logos.SonobiMobileAds.ExtraTrinityParams;

public class InterstitialFragment extends Fragment {

    private InterstitialViewModel interstitialViewModel;
    private PublisherInterstitialAd mPublisherInterstitialAd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        interstitialViewModel =
                ViewModelProviders.of(this).get(InterstitialViewModel.class);
        View root = inflater.inflate(R.layout.fragment_interstitial, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        interstitialViewModel.getText().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mPublisherInterstitialAd = new PublisherInterstitialAd(getActivity());
        mPublisherInterstitialAd.setAdUnitId("/7780971/ios-banner-test");

        ExtraTrinityParams extraTrinityParamManager = new ExtraTrinityParams();

        DfpInterstitialAd sonobiMobileInterstitialAd = new DfpInterstitialAd(mPublisherInterstitialAd, extraTrinityParamManager);
        sonobiMobileInterstitialAd.setTestMode(true);
        sonobiMobileInterstitialAd.setTimeout(15000);

        PublisherAdRequest.Builder adRequest = sonobiMobileInterstitialAd.requestBid(new PublisherAdRequest.Builder());

        mPublisherInterstitialAd.loadAd(adRequest.build());

        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (mPublisherInterstitialAd.isLoaded()) {
                    mPublisherInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
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
                // Code to be executed when the interstitial ad is closed.
            }
        });

        return root;
    }
}