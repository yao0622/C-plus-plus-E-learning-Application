package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.yaofa.client.util.AsyncTaskUtil;
import com.example.yaofa.client.util.DialogUtil;
import com.example.yaofa.client.util.HttpTask;
import com.example.yaofa.client.util.HttpUtil;
import com.example.yaofa.client.util.MapUrl;
import com.example.yaofa.client.util.OpenFileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by yaofa on 2016/5/24.
 */
public class ResList extends Activity {
    TextView resDirectory;
    ListView resList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reslist);
        resDirectory=(TextView) findViewById(R.id.resDirectory);
        resList=(ListView) findViewById(R.id.resourceList);
        final List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        String fileNameListUrl= HttpUtil.BASE_URL+"/file/listResources";
        MapUrl mapUrl=new MapUrl();
        mapUrl.setUrl(fileNameListUrl);
        Map<String,String> map=new HashMap<>();
        map.put("url",fileNameListUrl);
        mapUrl.setJsonString(map);
        HttpTask httpTask=new HttpTask();
        Integer code=-1;
         // List<String> fileNameList=new ArrayList<String>();
        try {
            JSONObject jsonObject=httpTask.execute(mapUrl).get();
            try{
                JSONArray jsonArray=jsonObject.getJSONArray("fileNameList");
                for(int i=0;i<jsonArray.length();i++){
                   // fileNameList.add(jsonArray.getString(i));
                    Map<String,Object>listItem=new HashMap<String,Object>();
                    listItem.put("name",jsonArray.getString(i));
                    listItems.add(listItem);
                }
                code=jsonObject.getInt("code");
                if(code!=0){
                    DialogUtil.showDialog(ResList.this,"服务器响应异常，请稍后再试！",false);
                }
            }
            catch (JSONException e){
                DialogUtil.showDialog(ResList.this,"服务器响应异常，请稍后再试！",false);
                e.printStackTrace();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e){
            DialogUtil.showDialog(ResList.this,"服务器响应异常，请稍后再试！",false);
            e.printStackTrace();
        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,
                R.layout.res_tittle_item,new String[]{"name"},new int[]{R.id.resTittle});

        resList.setAdapter(simpleAdapter);

        resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tmpStr=listItems.get(position).get("name").toString();
                tmpStr=tmpStr.replaceAll(" ","");
                String downloadUrl=HttpUtil.BASE_URL+"/file/downloadFile?filename="+tmpStr;
                String detailUrl=HttpUtil.BASE_URL+"/file/findFileDetail";
                Map<String,String> map=new HashMap<String, String>();
                map.put("fileName",tmpStr);
                MapUrl mapUrl1=new MapUrl();
                mapUrl1.setUrl(detailUrl);
                mapUrl1.setJsonString(map);
                HttpTask httpTask1=new HttpTask();
                Integer code=-1;
                String suffix="";

                try {
                    JSONObject jsonObject=httpTask1.execute(mapUrl1).get();
                    try {
                        code=jsonObject.getInt("code");
                        if(code.equals(0)){
                            suffix=jsonObject.getString("suffix");
                        }
                    } catch (JSONException e) {
                        DialogUtil.showDialog(ResList.this,"服务器响应异常，请稍后再试！",false);
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String tmpFileName=tmpStr+"."+suffix;
                String filepath= Environment.getExternalStorageDirectory()+"/download/"+tmpFileName;
                File file=new File(filepath);
                if (file.exists()){
                    Intent intent= OpenFileUtil.openFile(filepath);
                    startActivity(intent);
                }
                else {
                   /* DownTask downTask=new DownTask();
                    downTask.execute(downloadUrl,tmpFileName);*/
                    Handler handler=new Handler();
                    AsyncTaskUtil asyncTaskUtil=new AsyncTaskUtil(ResList.this,handler);
                    asyncTaskUtil.execute(downloadUrl,tmpFileName);
                }
            }
        });
    }
}