package com.htyhbz.yhyg.receiver;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;
import com.htyhbz.yhyg.InitApp;
import com.htyhbz.yhyg.utils.PrefUtils;

import java.util.Date;
import java.util.Random;
/**
 * Created by zongshuo on 2017/8/27 0027.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(downId == PrefUtils.getLong(context, InitApp.DOWNLOAD_TASK_PREF, InitApp.DOWNLOAD_TASK_ID_KEY, 0)) {
                Toast.makeText(context, "烟花易购客户端下载完成！", Toast.LENGTH_LONG).show();
                String fileName = "";
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);//从下载服务获取下载管理器
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downId);
                Cursor c = downloadManager.query(query);// 查询以前下载过的‘成功文件’
                if (c.moveToFirst()) {// 移动到最新下载的文件
                    fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                }
//            File f = new File(fileName.replace("file://", ""));// 过滤路径
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                intent1.addCategory("android.intent.category.DEFAULT");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setDataAndType(Uri.parse(fileName), "application/vnd.android.package-archive");//Uri.fromFile(f)
                context.startActivity(intent1);
            } else if(downId == PrefUtils.getLong(context, InitApp.DOWNLOAD_TASK_PREF, InitApp.DOWNLOAD_ERCODE_TASK_ID_KEY, 0)) {
                String fileName = "";
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);//从下载服务获取下载管理器
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downId);
                Cursor c = downloadManager.query(query);// 查询以前下载过的‘成功文件’
                if (c.moveToFirst()) {// 移动到最新下载的文件
                    fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                }
                Toast.makeText(context, "保存路径：" + fileName, Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    Intent intent1 = new Intent();
                    intent1.setAction("android.intent.action.VIEW");
                    intent1.addCategory("android.intent.category.DEFAULT");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.setDataAndType(Uri.parse(fileName), "image/*");

                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);// | Intent.FLAG_ACTIVITY_CLEAR_TOP

                    PendingIntent contentIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                    Random random = new Random(System.currentTimeMillis());

                    Notification notification = new Notification();
                    notification.when = new Date().getTime();
                    notification.icon = android.R.drawable.stat_sys_download_done;
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                    notification.setLatestEventInfo(context, "图片下载", "下载已完成", contentIntent);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(random.nextInt(), notification);
                }
            }
        }
    }
}