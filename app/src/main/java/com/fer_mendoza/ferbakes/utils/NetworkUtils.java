package com.fer_mendoza.ferbakes.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

final public class NetworkUtils {

    private NetworkUtils() {}

    public static URL parseURL(String url, HashMap<String, String> params){
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .path(url);

        for (String key: params.keySet()) {
            builder.appendQueryParameter(key, params.get(key));
        }

        URL wellUrl = null;
        try{
            wellUrl = new URL(builder.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return wellUrl;
    }
}