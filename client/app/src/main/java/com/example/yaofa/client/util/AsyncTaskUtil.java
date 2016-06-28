package com.example.yaofa.client.util;

/**
 Created by yaofa on 2016/5/26.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class AsyncTaskUtil extends AsyncTask<String, Double, Boolean> {
    public static final int NOTIFICATION_PROGRESS_UPDATE = 1;//用于更新下载进度的标志
    public static final int NOTIFICATION_PROGRESS_SUCCEED = 2;//表示下载成功
    public static final int NOTIFICATION_PROGRESS_FAILED = 3;//表示下载失败
    //URL
    private String mUrl;
    //activity
    private Context mContext;
    //任务定时器
    private Timer mTimer;
    //定时任务
    private TimerTask mTask;
    //主线程传递过来的handler
    private Handler  mHandler;
    //已下载的文件大小
    private long mTotalReadSize;
    //AsyncTaskRunnable实现了Runnable接口，用于更新下载进度的显示
    private AsyncTaskRunnable mRunnable;

    //构造方法
    public AsyncTaskUtil(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        mTimer = new Timer();
        mTask = new TimerTask() {//在run方法中执行定时的任务
            @Override
            public void run() {
                //通过AsyncTaskRunnable的setDatas方法下载的进度和状态（更新中、失败、成功）
                mRunnable.setDatas(NOTIFICATION_PROGRESS_UPDATE, mTotalReadSize);
                //更新进度
                mHandler.post(mRunnable);
            }
        };
        mRunnable = new AsyncTaskRunnable(mContext);
    }

    // 执行耗时操作,params[0]为url，params[1]为文件名（空则写入null）
    @Override
    protected Boolean doInBackground(String... params) {
        mTimer.schedule(mTask, 0, 500);
        try {
            mUrl = params[0];
            //建立链接
            URLConnection connection = new URL(mUrl).openConnection();
            InputStream is = connection.getInputStream();
            //先建立文件夹
            File fold = new File(getFolderPath());
            if (!fold.exists()){
                fold.mkdirs();
            }
            String fileName = "";
            //判断文件名：用户自定义或由url获得
            if(params[1] != null){
                fileName = params[1];
            } else{
                fileName = getFileName(params[0]);
            }
            //文件输出流
            FileOutputStream fos = new FileOutputStream(new File(getFolderPath()
                    + fileName));

            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) != -1) {
                mTotalReadSize += len;
                fos.write(buff, 0, len);
            }
            fos.flush();
            fos.close();

        } catch (Exception e) {
            //异常,下载失败
            mRunnable.setDatas(NOTIFICATION_PROGRESS_FAILED, 0L);
            //发送显示下载失败
            mHandler.post(mRunnable);
            if(mTimer != null && mTask != null){
                mTimer.cancel();
                mTask.cancel();
            }
            e.printStackTrace();
            return false;
        }
        //下载成功
        mRunnable.setDatas(NOTIFICATION_PROGRESS_SUCCEED, 0L);
        mHandler.post(mRunnable);
        if(mTimer != null && mTask != null){
            mTimer.cancel();
            mTask.cancel();
        }
        return true;
    }

    //由url获得文件名
    private String getFileName(String string) {
        return string.substring(string.lastIndexOf("/") + 1);
    }

    //下载文件夹路径
    private String getFolderPath() {
        return Environment.getExternalStorageDirectory().toString() + "/download/";
    }
    // doInBackground方法之前调用，初始化UI
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    // 在doInBackground方法之后调用
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            Toast.makeText(mContext, "Download completed ! ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Dwnload failed ! ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
    }
}
