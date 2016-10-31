package com.example.semalkan.electionprediction;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public List<JSONObject> getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            URL myUrl = new URL(url);
            HttpURLConnection httpClient = (HttpURLConnection) myUrl.openConnection();

            is = new BufferedInputStream(httpClient.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        JSONArray jObj = null;
        try {
            jObj = new JSONArray(json);
            // return JSON String
            int len = jObj.length();
            ArrayList<JSONObject> l = new ArrayList<JSONObject>();
            for(int i = 0; i < len; i++) {
                l.add(jObj.getJSONObject(i));
            }
            return l;
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            return null;
        }


    }
}