package gaoyanshan.client.download;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;

import gaoyanshan.client.test.MainActivity;


public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    private Socket socket;;
    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private String downloadIp;
    private int port;
    private String fileName;


    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        RandomAccessFile savedFile = null;
        File file = null;
        DataInputStream dis;
        downloadIp=params[0];
        port=Integer.parseInt(params[1]);
        fileName=params[2];
        Long downloadLength=Long.parseLong(params[3]);
        long savedLength=0;
        try {
            InetSocketAddress address = new InetSocketAddress(downloadIp, port);
            socket = new Socket();
            socket.connect(address, 2000);
            socket.setSoTimeout(8000);

            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory +"//"+ fileName);

            if(file.exists()){
                savedLength=file.length()-(file.length()%1024);//如果文件存在记录其大小
            }
            //向服务器发送当前文件大小
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            OutputStreamWriter writer=new OutputStreamWriter(os,"UTF-8");
            writer.write(savedLength+"\n");//未真正写入的输出流，仅仅在内存中
            writer.flush();//写入输出流，真正将数据传输出去
            dis = new DataInputStream(socket.getInputStream());
            // 开始接收文件
            byte[] bytes = new byte[1024];//缓存大小
            int length = 0;
            int total=0;//记录写入过程的长度
            savedFile=new RandomAccessFile(file,"rw");
            savedFile.seek(savedLength);
            while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                if(isCanceled)
                    return TYPE_CANCELED;
                else if(isPaused)
                    return TYPE_PAUSED;
                else
                    total+=length;
                savedFile.write(bytes,0,length);
                int progress=(int)((total+savedLength)*100/downloadLength);
                publishProgress(progress);
            }
            return TYPE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(fileName,progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }


    public void cancelDownload() {
        isCanceled = true;
    }



}