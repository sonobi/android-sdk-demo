package com.sonobi.logos.ui.video;

import android.app.Activity;
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

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.sonobi.logos.R;
import com.sonobi.sonobimobileads.DemandFetch;
import com.sonobi.sonobimobileads.DemandFetchCallbackHandler;
import com.sonobi.sonobimobileads.ExtraTrinityParams;

public class VideoFragment extends Fragment {

    private VideoViewModel videoViewModel;
    private RewardedAd rewardedAd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoViewModel =
                ViewModelProviders.of(this).get(VideoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_video, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        videoViewModel.getText().observe(this.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        rewardedAd = new RewardedAd(getActivity(), "/7780971/apex_prebid_video");

        final RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {

                // Ad successfully loaded.
                if (rewardedAd.isLoaded()) {

                    Activity activityContext = getActivity();
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display.
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };

        ExtraTrinityParams extraTrinityParamManager = new ExtraTrinityParams();
        final PublisherAdRequest.Builder adRequest = new PublisherAdRequest.Builder();

        DemandFetch sonobiDemandFetcher = new DemandFetch("", "5ff883840f91f2f48d18", extraTrinityParamManager, new DemandFetchCallbackHandler() {
            @Override
            public void onComplete(String resultCode) {
                System.out.println(adRequest);
                System.out.println(adRequest.build().getCustomTargeting());
                rewardedAd.loadAd(adRequest.build(), adLoadCallback);

            }
        });
        sonobiDemandFetcher.setTestMode(true);
        sonobiDemandFetcher.setTimeout(15000);


        sonobiDemandFetcher.requestBid(adRequest);


        return root;
    }
}