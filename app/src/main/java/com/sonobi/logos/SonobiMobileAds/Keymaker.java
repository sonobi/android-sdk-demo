package com.sonobi.logos.SonobiMobileAds;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by jgo on 10/5/17.
 */

public class Keymaker {

    //Constant query params
    private final static String BASE_TRINITY_URL = "https://apex.go.sonobi.com/trinity.json";
    private final static String CV = "sbi";
    private final static String VP = "mobile";
    private final static String S = UUID.randomUUID().toString();
    private final static String PV = UUID.randomUUID().toString();
    private final static String VC = "0";

    String slotKey;
    private String url;
    private String method;
    private JSONObject body;

    public Keymaker(Integer id, String adUnitId, String sizesCsv, ExtraTrinityParams extraTrinityParams) {

        String hfa = extraTrinityParams.getHfa(),
                cdf = extraTrinityParams.getCdf(),
                ant = extraTrinityParams.getAnt(),
                gmgt = extraTrinityParams.getGmgt(),
                floor = extraTrinityParams.getFloor();
        String url = BASE_TRINITY_URL;

        System.out.println(adUnitId + " Sonobi");

        if(adUnitId.charAt(0) == '/') {
            this.slotKey = adUnitId + "|" + id;
            url += "?key_maker={\"" + this.slotKey + "\":\"" + sizesCsv;
        } else {
            Log.d("SonobiMobileAds", adUnitId);
            this.slotKey = id.toString();
            url += "?key_maker={\"" + this.slotKey + "\":\""+ adUnitId + "|" + sizesCsv;
        }


        if(floor.length() > 0) {
            url += "|f=" + floor;
        }

        url += "\"}";
        //url += "{\"mobile-test\":\"f7d3088e7e7e9a2b1126\"}";
        url += "&cv=" + CV;

        url += "&vp=" + VP;

        //cache buster
        url += "&s=" + S;

        //page view id
        url += "&pv=" + PV;

        url += "&vc=" + VC;



        if (hfa.length() > 0) {
            url += "&hfa=" + hfa;
        }

        if (cdf.length() > 0) {
            url += "&cdf=" + cdf;
        }

        if (ant.length() > 0) {
            url += "&ant=" + ant;
        }

        if (gmgt.length() > 0) {
            url += "&gmgt=" + gmgt;
        }

        this.url = url;
        this.method = "GET";
    }

    public JSONObject executeRequest(Integer timeout, Boolean testMode) {

        HttpsRequest httpsRequest = new HttpsRequest(this.method, timeout, testMode, this.body);
        Object rawResponse;
        JSONObject parsedResponse = new JSONObject();
        try {
            try {

                rawResponse = httpsRequest.execute(this.url).get().toString();
                parsedResponse = this.parseResponse(rawResponse);

            } catch (ExecutionException e) {
                return parsedResponse;
            }
        } catch (InterruptedException e) {
            return parsedResponse;
        }

        return parsedResponse;

    }

    public JSONObject parseResponse(Object rawResponse) {

        JSONObject parsedResponse;
        String stringedResponse = rawResponse.toString();

        try {
            parsedResponse = new JSONObject(stringedResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            parsedResponse = new JSONObject();
        }

        return parsedResponse;

    }


}
