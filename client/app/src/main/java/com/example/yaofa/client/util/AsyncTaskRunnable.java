package com.example.yaofa.client.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.yaofa.client.R;

import java.text.DecimalFormat;

public class AsyncTaskRunnable implements Runnable{
    //主线程的activity
    private Context mContext;
    //notification的状态：更新 or 失败 or 成功
    private int mStatus;
    //notification的下载多少
    private Long mSize;
    //管理下拉菜单的通知信息
    private NotificationManager mNotificationManager;
    //下拉菜单的通知信息
    private Notification mNotification;
    //下拉菜单的通知信息的view
    private RemoteViews mRemoteViews;
    //下拉菜单的通知信息的种类id
    private static final int NOTIFICATION_ID = 1;

    //设置比例和数据
    public void setDatas(int status ,Long size) {
        this.mStatus = status;
        this.mSize = size;
    }
    //初始化
    public AsyncTaskRunnable(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //初始化下拉菜单的通知信息
        mNotification = new Notification();
        mNotification.icon = R.drawable.downloadicon;//设置的icon
        mNotification.tickerText = mContext.getResources().getString(R.string.app_name); //设置下载进度的title
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.downloadmenu);
        mRemoteViews.setImageViewResource(R.id.download_icon, R.drawable.downloadicon);
    }

    @Override
    public void run() {//通过判断不同的状态：更新中/下载失败/下载成功 更新下拉菜单的通知信息
        switch (mStatus) {
            case AsyncTaskUtil.NOTIFICATION_PROGRESS_FAILED://下载失败
                mNotificationManager.cancel(NOTIFICATION_ID);
                break;

            case AsyncTaskUtil.NOTIFICATION_PROGRESS_SUCCEED://下载成功
                mRemoteViews.setTextViewText(R.id.id_download_textview, "Download completed ! ");
                mNotification.contentView = mRemoteViews;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                mNotificationManager.cancel(NOTIFICATION_ID);
                Toast.makeText(mContext, "Download completed ! ", Toast.LENGTH_SHORT).show();
                break;

            case AsyncTaskUtil.NOTIFICATION_PROGRESS_UPDATE://更新中
                DecimalFormat format = new DecimalFormat("0");//数字格式转换
                String progress = format.format((int)(mSize/(1024)));
                mRemoteViews.setTextViewText(R.id.id_download_textview, "Downloading: " + progress + "KB！");
                mNotification.contentView = mRemoteViews;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                break;
        }
    }
}