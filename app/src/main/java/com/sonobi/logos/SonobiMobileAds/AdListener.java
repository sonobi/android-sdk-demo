package com.sonobi.logos.SonobiMobileAds;

import android.view.View;

import androidx.annotation.Keep;

/**
 * Created by jgo on 10/11/17.
 */

@Keep
public interface AdListener {
    void onError(String errorMessage);
    void onAdLoaded(View view);
}
