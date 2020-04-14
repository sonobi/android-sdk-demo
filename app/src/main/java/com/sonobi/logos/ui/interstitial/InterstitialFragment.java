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
import com.sonobi.sonobimobileads.DemandFetch;
import com.sonobi.sonobimobileads.DemandFetchCallbackHandler;
import com.sonobi.sonobimobileads.ExtraTrinityParams;

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
        final PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();

        ExtraTrinityParams extraTrinityParamManager = new ExtraTrinityParams();
        DemandFetch sonobiDemandFetcher = new DemandFetch("", mPublisherInterstitialAd.getAdUnitId(), extraTrinityParamManager, new DemandFetchCallbackHandler() {
            @Override
            public void onComplete(String resultCode) {
                mPublisherInterstitialAd.loadAd(adRequest.build());
            }
        });

        sonobiDemandFetcher.setTestMode(true);
        sonobiDemandFetcher.setTimeout(15000);

        sonobiDemandFetcher.requestBid(adRequest);


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