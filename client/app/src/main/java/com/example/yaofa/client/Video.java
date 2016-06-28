package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.yaofa.client.util.AsyncTaskUtil;
import com.example.yaofa.client.util.DialogUtil;
import com.example.yaofa.client.util.HttpTask;
import com.example.yaofa.client.util.HttpUtil;
import com.example.yaofa.client.util.MapUrl;
import com.example.yaofa.client.util.OpenFileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by yaofa on 2016/4/29.
 */
public class Video extends Activity {
    VideoView videoPlayer;
    int videoId;
    MediaController mediaController;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       setContentView(R.layout.video);
       // videoPlayer=(VideoView) findViewById(R.id.videoPlayer);
        Intent intent=getIntent();
        videoId=intent.getIntExtra("id",-1);
        if(videoId<0){
            DialogUtil.showDialog(Video.this,"无法找到你所需要的内容",false);
        }
        Resources res=getResources();
        String[] list=res.getStringArray(R.array.videoTittleList);
        String videoName=list[videoId];
        //String url= HttpUtil.BASE_URL+"/video/downFile?filename="+videoName;

        String detailUrl=HttpUtil.BASE_URL+"/file/findFileDetail";
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
                DialogUtil.showDialog(Video.this,"服务器响应异常，请稍后再试！",false);
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
            AsyncTaskUtil asyncTaskUtil=new AsyncTaskUtil(Video.this,handler);
            asyncTaskUtil.execute(url,fileFullName);

        }

 /*       String url= HttpUtil.BASE_URL+"/file/downloadFile?filename="+videoName;
        //  videoPlayer.setVideoPath(url);
        videoPlayer.setVideoURI(Uri.parse(url));
        //MediaController
        mediaController=new MediaController(this);
            //设置播放视频源的路径
        //为VideoView指定MediaController
        videoPlayer.setMediaController(mediaController);
        //为MediaController指定控制的VideoView
        mediaController.setMediaPlayer(videoPlayer);
        //让videoView获取焦点
        videoPlayer.requestFocus();

        //增加监听上一个和下一个的切换事件，默认这两个按钮是不显示的
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(Video.this, "下一个", Toast.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(Video.this,"上一个",Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoPlayer.setLayoutParams(layoutParams);
      //  videoPlayer.start();*/

    }
}
