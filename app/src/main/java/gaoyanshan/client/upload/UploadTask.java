package gaoyanshan.client.upload;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;


public class UploadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    private Socket socket;;
    private UploadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private String uploadIp;
    private int port;
    private String filePath;

    private Long fileLength;
    public UploadTask(UploadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        uploadIp=params[0];
        port=Integer.parseInt(params[1]);
        filePath=params[2];
        DataOutputStream dos=null;
        try {
            InetSocketAddress address = new InetSocketAddress(uploadIp, port);
            socket = new Socket();
            socket.connect(address, 2000);
            socket.setSoTimeout(8000);

            System.out.println("UploadTask:"+Thread.currentThread());
            InputStream inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader=new BufferedReader(reader);

            File file=new File(filePath);
            fileLength=file.length();
            if(file.exists()) {
                dos = new DataOutputStream(socket.getOutputStream());
                RandomAccessFile access = new RandomAccessFile(file, "rw");
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                Long total=Long.parseLong("0");//记录上传过程的长度
                while ((length = access.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    if(isCanceled)
                        return TYPE_CANCELED;
                    else if(isPaused)
                        return TYPE_PAUSED;
                    else
                        total+=length;
                    int progress=(int)(total*100/fileLength);
                    System.out.print(progress+"   "+ fileLength+"\n");
                    publishProgress(progress);
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
                if (access != null)
                    access.close();
                if (dos != null)
                    dos.close();
                socket.close();

            }
            return TYPE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
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

    public void pauseUpload() {
        isPaused = true;
    }


    public void cancelUpload() {
        isCanceled = true;
    }



}