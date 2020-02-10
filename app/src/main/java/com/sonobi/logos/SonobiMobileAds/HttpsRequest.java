package com.sonobi.logos.SonobiMobileAds;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jgo on 10/4/17.
 */

public class HttpsRequest extends AsyncTask<Object, Void, Object> {

    private String methodType;
    private Integer timeout;
    private Boolean testMode = false;
    private JSONObject body;

    public HttpsRequest(String methodType, Integer timeout, Boolean testMode) {
        this.methodType = methodType;
        this.timeout = timeout;
        this.testMode = testMode;
    }

    public HttpsRequest(String methodType, Integer timeout, Boolean testMode, JSONObject body) {
        this.methodType = methodType;
        this.timeout = timeout;
        this.testMode = testMode;
        this.body = body;
    }

    @Override
    public Object doInBackground(Object[] objects) {
        String url = objects[0].toString();
        Object result;
        String inputLine;

        try {
            URL myUrl = new URL(url);

            HttpsURLConnection connection = (HttpsURLConnection) myUrl.openConnection();

            connection.setRequestMethod(this.methodType);
            connection.setReadTimeout(this.timeout);
            connection.setConnectTimeout(this.timeout);

            if (this.testMode) {
                connection.setRequestProperty("Cookie", "sbi_test={\"sbi_apoc\":\"guarantee\",\"sbi_mouse\":20}; domain=go.sonobi.com; path=/");
            }

            if(this.body != null) { //Write the body to the connection
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                connection.setDoOutput(true);
                connection.setDoInput(true);
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(this.body.toString());

                os.flush();
                os.close();
            } else {
                connection.connect();
            }

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            System.out.println(result);


        } catch (IOException e) {
            e.printStackTrace();
            result = "";
        }

        return result;
    }

    @Override
    public void onPostExecute(Object result) {
        super.onPostExecute(result);
    }
}
