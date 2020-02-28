package com.sonobi.logos.SonobiMobileAds;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.Keep;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jgo on 10/4/17.
 */

/**
 * Class to request bids from sonobi and set targeting on the ad request
 */
public class DemandFetch extends SonobiConfig {

    private String sizes;
    private ExtraTrinityParams extraTrinityParams;
    private String adUnit;
    private Handler handler;
    private DemandFetchCallbackHandler callback;
    /**
     * Constructor
     * @param sizes {String} A csv of sizes
     * @param adUnit {String} extra targeting The Ad Unit Code /123123/example/ad/unit
     * @param extraTrinityParams {ExtraTrinityParams} extra targeting parameters to pass to the trinity request
     */
    @Keep
    public DemandFetch(String sizes, String adUnit, ExtraTrinityParams extraTrinityParams, final DemandFetchCallbackHandler callback) {
        super();
        final DemandFetch self = this;
        this.sizes = sizes;
        this.extraTrinityParams = extraTrinityParams;
        this.adUnit = adUnit;
        this.callback = callback;
        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                self.callback.onComplete(msg.toString());
            }

        };
    }

    /**
     * Method to request a bid from trinity
     *
     * @param adRequest {PublisherAdRequest.Builder} The ad request builder to add sbi_* targeting to
     */
    @Keep
    public void requestBid(final PublisherAdRequest.Builder adRequest) {
        final DemandFetch self = this;
        //Body of your click handler
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //code to do the HTTP request

                String sizes = self.sizes;
                JSONObject keymakerResponse;
                JSONObject bidResponse = new JSONObject();

                //Targeting variables;
                String sbiDc;
                Double sbiPrice;
                String sbiAid;
                String sbiDozer;

                //Form our CSV of sizes
                if(sizes.length() > 0 && sizes.charAt(sizes.length() - 1) == ',') {
                    sizes = sizes.substring(0, sizes.length() - 1);
                }


                //Do the trinity request
                Keymaker sonobiKeymaker = new Keymaker(1, adUnit, sizes, self.extraTrinityParams);
                keymakerResponse = sonobiKeymaker.executeRequest(self.getTimeout(), self.isTestMode());

                //try to get sbi_dc from the response
                try {
                    sbiDc = keymakerResponse.getString("sbi_dc");
                    adRequest.addCustomTargeting("sbi_dc", sbiDc);
                } catch (JSONException e) { //if it errors, return the adRequest
                    e.printStackTrace();
                    self.handler.sendMessage(new Message());
                }

                //try to get the bid response
                try {
                    bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject(sonobiKeymaker.slotKey);
                    //bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject("mobile-test");
                } catch (JSONException e) { //if it errors, return the adRequest
                    e.printStackTrace();
                    self.handler.sendMessage(new Message());
                }

                try { //try to get the cpm value
                    sbiPrice = bidResponse.getDouble("sbi_mouse");
                    adRequest.addCustomTargeting("sbi_price", sbiPrice.toString());
                } catch (JSONException e) { //if it errors, return the adRequest
                    e.printStackTrace();
                    self.handler.sendMessage(new Message());

                }

                try { //try to get the aid
                    sbiAid = bidResponse.getString("sbi_aid");
                    adRequest.addCustomTargeting("sbi_aid", sbiAid);
                } catch (JSONException e) { //if it errors, return the adRequest
                    e.printStackTrace();
                    self.handler.sendMessage(new Message());
                }

                try { //try to get the dozer
                    sbiDozer = bidResponse.getString("sbi_dozer");
                    adRequest.addCustomTargeting("sbi_dozer", sbiDozer);
                } catch (JSONException e) { //if it errors, return the adRequest
                    e.printStackTrace();
                }

                self.handler.sendMessage(new Message());

            }
        });
        thread.start();







    }

}
