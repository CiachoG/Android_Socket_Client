package gaoyanshan.client.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import com.example.ciacho.gys_socket.R;


import java.io.File;
import java.util.List;

import gaoyanshan.client.app.MyActivity;
import gaoyanshan.client.operator.SendMsgOperator;

import static android.content.Context.NOTIFICATION_SERVICE;

public class UploadProgressDialog extends ProgressDialog {
    private UploadTask uploadTask;

    private String uploadIp;

    private String uploadPort;

    private String uploadPath;


    private int lastProgress=0;

    private NotificationManager notificationManager;

    public UploadProgressDialog(Context context) {
        super(context);
        this.setTitle("上传文件");
        this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.setButton(DialogInterface.BUTTON_POSITIVE, "暂停",
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadTask.pauseUpload();

                        show();
                    }
                });
        this.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        uploadTask.cancelUpload();
                        dismiss();
                    }
                });
        this.setButton(DialogInterface.BUTTON_NEUTRAL, "最小化",
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //uploadBinder.startupload(uploadIp,uploadPort,uploadFileName,fileLenght);
                        notificationManager=getNotificationManager();
                        notificationManager.notify(2,getNotification(new File(uploadPath).getName()+"上传中",lastProgress));

                        dismiss();
                        // TODO Auto-generated method stub

                    }
                });

    }

    private UploadListener listener = new UploadListener() {
        @Override
        public void onProgress(int progress) {
            incrementProgressBy(progress);
            if (notificationManager!=null)
            {
                notificationManager.notify(2,getNotification(new File(uploadPath).getName()+"上传中",progress));
            }
        }

        @Override
        public void onSuccess() {
            if(notificationManager!=null)
                notificationManager.notify(2,getNotification("上传成功",-1));
                List<String> mData=MyActivity.mHandler.getmRData();
                String str ="";
                for(int i=1;i<mData.size();i++)
                {
                    str=str+mData.get(i)+"\\";
                }
                System.out.println(str);
                new SendMsgOperator(MyActivity.mHandler,getContext()).dir(str);
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
    public UploadProgressDialog initParameter(String uploadIp, String uploadPort, String uploadPath){
        this.uploadIp=uploadIp;
        this.uploadPort=uploadPort;
        this.uploadPath=uploadPath;
        return this;
    }
    public UploadProgressDialog starUpload()
    {
        uploadTask=new UploadTask(listener);
        uploadTask.execute(uploadIp,uploadPort,uploadPath);
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
