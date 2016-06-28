package com.example.yaofa.client.util;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yaofa on 2016/4/27.
 */

public class HttpTask extends AsyncTask<MapUrl,Void,JSONObject> {
    @Override
    protected JSONObject doInBackground(MapUrl... params){
        MapUrl mapUrl=params[0];
        try {
            return new JSONObject(HttpUtil.post(mapUrl.getUrl(),mapUrl.getJsonString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}