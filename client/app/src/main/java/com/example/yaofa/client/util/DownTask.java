package com.example.yaofa.client.util;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yaofa on 2016/5/13.
 */
public class DownTask extends AsyncTask<String,Integer,String> {
    //params[0]为url
    //params[1]为文件名（加了后缀）
    //param[2] 为下载到SD卡上的目录

    public static final int NOTIFICATION_PROGRESS_UPDATE=1;//用于更新下载进度的标志
    public static final int NOTIFICATION_PROGRESS_SUCCEED=2;//表示下载成功
    public static final int NOTIFICATION_PROGRESS_FAILED=3;//表示下载失败

    private String murl;


    @Override
    protected String doInBackground(String... params){
        InputStream inputStream=null;
        OutputStream outputStream=null;
        //在SD卡创建下载目录
        String sdpath= Environment.getExternalStorageDirectory()+"/download/";
        File file=new File(sdpath);
        try{
            //如果文件夹不存在则创建
            if (!file.exists()) {
                file.mkdir();
            }
            URL url=new URL(params[0]);
            String fileName=params[1];
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6*1000);
            conn.connect();
            inputStream=conn.getInputStream();
            outputStream=new FileOutputStream(sdpath+fileName);
            //获取文件长度
            float fileSize=conn.getContentLength();
            byte buffer[]=new byte[1024];
            int count=0;
            int length=-1;
            while((length=inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,length);
                count+=length;
            }
            outputStream.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                outputStream.close();
                inputStream.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Integer...values){
        super.onProgressUpdate(values);
    }
}
