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
 * Created by yaofa on 2016/5/5.
 */
public class VideoList extends Activity {
    ListView videoList;
    TextView videoTittleList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videolist);
        videoList=(ListView) findViewById(R.id.videoListShow);
        videoTittleList=(TextView) findViewById(R.id.videoTittleList);
        //创建一个List集合，List集合的元素是Map
        List<Map<String,Object>> listItems= new ArrayList<Map<String,Object>>();
        //这个地方是可以扩展的，当每个
        // listItem要加其他东西的时候可以将其转化为一个类
        Resources res=getResources();
        String[] list=res.getStringArray(R.array.videoTittleList);
        for (int i=0;i<list.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("name",list[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,
                R.layout.video_tittle_item,new String[]{"name"},new int[]{R.id.videoTittle});
        //为ListView设置Adapter
        videoList.setAdapter(simpleAdapter);
        //为ListView的列表项的单击事件绑定事件监听器
        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //第i项被单击时激发该方法
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //跳转到另一个界面
               // Intent intent=new Intent(VideoList.this,Video.class);
                //intent.putExtra("id",position);
                //startActivity(intent);
                int videoId=position;

                if(videoId<0){
                    DialogUtil.showDialog(VideoList.this,"无法找到你所需要的内容",false);
                }
                Resources res=getResources();
                String[] list=res.getStringArray(R.array.videoTittleList);
                String videoName=list[videoId];
                //String url= HttpUtil.BASE_URL+"/video/downFile?filename="+videoName;

                String detailUrl= HttpUtil.BASE_URL+"/file/findFileDetail";
                Map<String,String> map=new HashMap<>();
                map.put("fileName",videoName);
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
                        DialogUtil.showDialog(VideoList.this,"服务器响应异常，请稍后再试！",false);
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String fileFullName=videoName+"."+suffix;
                String filepath= Environment.getExternalStorageDirectory()+"/download/"+fileFullName;
                File file=new File(filepath);
                if (file.exists()){
                    Intent intent1= OpenFileUtil.openFile(filepath);
                    startActivity(intent1);
                    // videoPlayer.setVideoPath(filepath);
                }
                else {
                    String url= HttpUtil.BASE_URL+"/file/downloadFile?filename="+videoName;
                    //  DownTask downTask=new DownTask();
                    //  downTask.execute(url,fileFullName);
                    Handler handler=new Handler();
                    AsyncTaskUtil asyncTaskUtil=new AsyncTaskUtil(VideoList.this,handler);
                    asyncTaskUtil.execute(url,fileFullName);
                }
            }
        });
    }
}