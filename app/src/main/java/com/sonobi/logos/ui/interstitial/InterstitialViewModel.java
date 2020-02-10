package com.sonobi.logos.ui.interstitial;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InterstitialViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InterstitialViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Interstitial Demo");
    }

    public LiveData<String> getText() {
        return mText;
    }
}