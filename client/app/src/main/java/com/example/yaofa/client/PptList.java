package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by yaofa on 2016/5/13.
 */
public class PptList extends Activity{
    TextView pptDirectory;
    ListView pptList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pptlist);
        pptDirectory = (TextView) findViewById(R.id.pptDirectory);
        pptList = (ListView) findViewById(R.id.pptList);
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        Resources resources = getResources();
        final String[] tittleList = resources.getStringArray(R.array.pptTittleList);
        for (int i = 0; i < tittleList.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("name", tittleList[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.ppt_tittle_item,
                new String[]{"name"}, new int[]{R.id.pptTittle});

        pptList.setAdapter(simpleAdapter);
        pptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tmpStr=tittleList[position].replaceAll(" ","");
                String downloadUrl= HttpUtil.BASE_URL+"/file/downloadFile?filename="+tmpStr;
                String detailUrl=HttpUtil.BASE_URL+"/file/findFileDetail";
                Map<String,String> map=new HashMap<>();
                map.put("fileName",tmpStr);
                MapUrl mapUrl=new MapUrl();
                mapUrl.setUrl(detailUrl);
                mapUrl.setJsonString(map);
                HttpTask httpTask=new HttpTask();
                Integer code=-1;
                String suffix="";
                try {
                    JSONObject jsonObject=httpTask.execute(mapUrl).get();
                    try {
                        code=jsonObject.getInt("code");
                        if(code.equals(0)){
                            suffix=jsonObject.getString("suffix");
                        }
                    } catch (JSONException e) {
                        DialogUtil.showDialog(PptList.this,"服务器响应异常，请稍后再试！",false);
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String tmpfilename=tmpStr+"."+suffix;
                String filepath=Environment.getExternalStorageDirectory()+"/download/"+tmpfilename;
                File file=new File(filepath);
                if (file.exists()){
                   Intent intent= OpenFileUtil.openFile(filepath);
                    startActivity(intent);
                }
                else {
                   // DownTask downTask=new DownTask();
                   // downTask.execute(downloadUrl,tmpfilename);
                    Handler handler=new Handler();
                    AsyncTaskUtil asyncTaskUtil=new AsyncTaskUtil(PptList.this,handler);
                    asyncTaskUtil.execute(downloadUrl,tmpfilename);
                }
            }
        });
    }
}
