package gaoyanshan.client.download;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;


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
    private int position;
    BufferedOutputStream os;
    OutputStreamWriter writer;
    private File file = null;
    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        RandomAccessFile savedFile = null;

        DataInputStream dis;
        downloadIp=params[0];
        port=Integer.parseInt(params[1]);
        fileName=params[2];
        Long downloadLength=Long.parseLong(params[3]);
        long savedLength=0;
        position=Integer.parseInt(params[4]);
        try {
            InetSocketAddress address = new InetSocketAddress(downloadIp, port);
            socket = new Socket();
            socket.connect(address, 2000);
            socket.setSoTimeout(8000);

            System.out.println(Thread.currentThread()+"======== 开始传输文件 ========");
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory +"//"+ fileName);

            if(file.exists()){
                savedLength=file.length()-(file.length()%1024);//如果文件存在记录其大小
            }
            //向服务器发送当前文件大小
            os = new BufferedOutputStream(socket.getOutputStream());
            writer=new OutputStreamWriter(os,"UTF-8");
            writer.write(savedLength+"\n");//未真正写入的输出流，仅仅在内存中


            writer.flush();//写入输出流，真正将数据传输出去
            dis = new DataInputStream(socket.getInputStream());
            // 开始接收文件
            byte[] bytes = new byte[1024];//缓存大小
            int length = 0;
            Long total=Long.parseLong("0");//记录写入过程的长度
            savedFile=new RandomAccessFile(file,"rw");
            savedFile.seek(savedLength);
            while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                if(isCanceled)
                {
                    writer.write("cancel"+"\n");//未真正写入的输出流，仅仅在内存中
                    writer.flush();//写入输出流，真正将数据传输出去
                    return TYPE_CANCELED;
                }

                else if(isPaused){
                    writer.write("pause"+"\n");//未真正写入的输出流，仅仅在内存中
                    writer.flush();//写入输出流，真正将数据传输出去
                    return TYPE_PAUSED;
                }

                else
                    total+=length;
                if((total+savedLength)>=downloadLength){
                    writer.write("ok"+"\n");//未真正写入的输出流，仅仅在内存中
                    writer.flush();//写入输出流，真正将数据传输出去
                    socket.close();
                    return TYPE_SUCCESS;
                }
                else
                {
                    writer.write("no"+"\n");//未真正写入的输出流，仅仅在内存中
                    writer.flush();//写入输出流，真正将数据传输出去
                }
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
            listener.onProgress(port,progress);
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

    public void startDownload() {
        isPaused = false;
    }
    public void cancelDownload() {
        isCanceled = true;
    }


}