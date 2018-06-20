package gaoyanshan.client.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import java.io.File;

import gaoyanshan.client.util.OpenFiles;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DownloadProgressDialog extends ProgressDialog {
    private DownloadTask downloadTask;

    private String downloadIp;

    private String downloadPort;

    private String downloadFileName;

    private String fileLenght;

    private int lastProgress=0;

    private NotificationManager notificationManager;

    public DownloadProgressDialog(Context context) {
        super(context);
        this.setTitle("下载文件");
        this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.setButton(DialogInterface.BUTTON_POSITIVE, "暂停",
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                        downloadTask.pauseDownload();

                        show();
                    }
                });
        this.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        downloadTask.cancelDownload();
                        dismiss();
                    }
                });
        this.setButton(DialogInterface.BUTTON_NEUTRAL, "最小化",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //downloadBinder.startDownload(downloadIp,downloadPort,downloadFileName,fileLenght);
                        notificationManager=getNotificationManager();
                        notificationManager.notify(1,getNotification("下载中",lastProgress));

                        dismiss();
                        // TODO Auto-generated method stub

                    }
                });
    }

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(String filename, int progress) {
            incrementProgressBy(progress);
            if (notificationManager!=null)
            {
                notificationManager.notify(1,getNotification("下载中",progress));
            }
        }

        @Override
        public void onSuccess() {
            if(notificationManager!=null)
                notificationManager.notify(1,getNotification("下载成功",-1));
            dismiss();
        }

        @Override
        public void onFailed() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onCanceled() {

        }
    };
    public DownloadProgressDialog initParameter(String downloadIp,String downloadPort,String downloadFileName,String fileLenght){
        this.downloadIp=downloadIp;
        this.downloadPort=downloadPort;
        this.downloadFileName=downloadFileName;
        this.fileLenght=fileLenght;
        return this;
    }
    public DownloadProgressDialog starDownload()
    {
        downloadTask=new DownloadTask(listener);
        downloadTask.execute(downloadIp,downloadPort,downloadFileName,fileLenght);
        return  this;
    }

    @Override
    public void incrementProgressBy(int progress) {
        super.incrementProgressBy(progress-lastProgress);
        lastProgress=progress;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    private NotificationManager getNotificationManager() {
        return (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        if (progress >= 0) {
            // 当progress大于或等于0时才需显示下载进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
    private void openAssignFolder(String path){

    }
}
