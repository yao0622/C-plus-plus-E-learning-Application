package com.example.yaofa.client.util;

import java.util.Map;

/**
 * Created by yaofa on 2016/4/28.
 */

public class MapUrl {
    private Map<String,String> jsonString;
    private String url;

    public MapUrl(){
        jsonString=null;
        url=null;
    }

    public MapUrl(String uRl,Map<String,String> jsoNString){
        url=uRl;
        jsonString=jsoNString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getJsonString() {
        return jsonString;
    }

    public void setJsonString(Map<String, String> jsonString) {
        this.jsonString = jsonString;
    }
}
