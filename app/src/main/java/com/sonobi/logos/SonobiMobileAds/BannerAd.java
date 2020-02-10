package com.sonobi.logos.SonobiMobileAds;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.annotation.Keep;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jgo on 10/11/17.
 */

/**
 * Sonobi Banner Ad for rendering sonobi ads without an ad server.
 */
public class BannerAd extends SonobiConfig {

    private static final String NO_FILL_RESPONSE = "No Fill";

    private AdSize size;
    private String adUnitId;
    private Context context;
    private AdListener adListener;
    private ExtraTrinityParams extraTrinityParams;

    /**
     * @param context  The context to render the ad to
     * @param adUnitId The /ad/unit/id or a sonobi placement_id
     * @param size     WxH string
     */
    @Keep
    public BannerAd(Context context, String adUnitId, AdSize size, ExtraTrinityParams extraTrinityParams) {
        super();
        this.adUnitId = adUnitId;
        this.size = size;
        this.context = context;
        this.extraTrinityParams = extraTrinityParams;

    }

    @Keep
    public void setAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    private String formSbijs(String dataCenter, String aId) {

        return "<html><body style='margin: 0'><script type='text/javascript' src='https://"+dataCenter+"apex.go.sonobi.com/sbi.js?aid=" + aId + "&as=null'></script></body></html>";

    }

    private View renderAd(String sbiJsTag) {
        System.out.println(sbiJsTag);
        WebView.setWebContentsDebuggingEnabled(true);
        WebView webView = new WebView(this.context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, sbiJsTag, "text/html", "UTF-8", null);
        webView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        this.adListener.onAdLoaded(webView);

        return webView;
    }

    @Keep
    public View load() {

        JSONObject keymakerResponse;
        JSONObject bidResponse;

        //Targeting variables
        String sbiDc;
        String sbiAid;

        //Do the trinity request
        Keymaker sonobiKeymaker = new Keymaker(1, this.adUnitId, this.size.getSize(), this.extraTrinityParams);
        keymakerResponse = sonobiKeymaker.executeRequest(this.getTimeout(), this.isTestMode());

        //try to get sbi_dc from the response
        try {
            sbiDc = keymakerResponse.getString("sbi_dc");
        } catch (JSONException e) { //if it errors, return the adRequest
            this.adListener.onError(NO_FILL_RESPONSE);
            return new WebView(this.context);
        }

        //try to get the bid response
        try {
            bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject(this.adUnitId + "|" + 1);
        } catch (JSONException e) { //if it errors, return the adRequest
            this.adListener.onError(NO_FILL_RESPONSE);
            return new WebView(this.context);
        }

        try { //try to get the aid
            sbiAid = bidResponse.getString("sbi_aid");
        } catch (JSONException e) {
            this.adListener.onError(NO_FILL_RESPONSE);
            return new WebView(this.context);
        }

        return this.renderAd(this.formSbijs(sbiDc, sbiAid));

    }

}
