package gaoyanshan.client.socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import gaoyanshan.client.jsonBean.Entity2json;

/**
 * Created by Ciacho on 2018/3/9.
 */

public class ClientSocket {
    int port;
    String ip;
    int connect_timeout = 2000;//  设置 socket 连接的超时时间，单位： ms
    Handler handler;
    Socket socket;
    String set;
    OutputStreamWriter writer;
    BufferedReader bufferedReader;
    public static final String KEY_SERVER_ACK_MSG = "KEY_SERVER_ACK_MSG";
    public ClientSocket(String ip, int port, Handler handler,String set) {
        super();
        this.port = port;
        this.ip = ip;
        this.handler = handler;
        this.set=set;
    }
    private void doTask() {
        String msg  =null;
        try {
            connect();
            msg  = getMessage();
        } catch (IOException e) {
            msg="";
            e.printStackTrace();
        }

        close();
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SERVER_ACK_MSG, msg);
        message.setData(bundle);
        //Log.e("msg",msg);
        if(msg==null || msg==""||msg.equals("error"))
        {
            message.what=0;
        }
        else if(new Entity2json().isJson(msg)){
            message.what=1;
        }
        else  if(msg.indexOf("open")!=-1)
        {
            message.what=2;
        }
        else if(msg.equals("mov")){
            message.what=3;
        }
        else if(msg.equals("key")){
            message.what=4;
        }
        else if(msg.indexOf("dlf")!=-1){
            message.what=5;
        }
        else if(msg.indexOf("ulf")!=-1){
            message.what=6;
        }
        else if(msg.indexOf("del")!=-1)
        {
            message.what=7;
        }
        handler.sendMessage(message);//  通过句柄通知主 UI 数据传输完毕，并回传数据
    }
    public void work() {
        new Thread(new Runnable() {// Socket 的连接以及数据传输需在新开辟线程中工作，若在主 UI 线程中阻塞操作会使程序崩溃
            @Override
            public void run() {
// TODO Auto-generated method stub
                doTask();
            }
        }).start();
    }
    private void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        socket = new Socket();
        socket.connect(address, connect_timeout);
        socket.setSoTimeout(8000);

    }
    private String getMessage() {
        writeData();
        return readData();
    }
    private void close() {
        try {
            if(writer!=null)writer.close();

            if(bufferedReader!=null)bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  void  writeData(){
        try {
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            writer=new OutputStreamWriter(os,"UTF-8");//尝试将字符编码改成"GB2312"
            writer.write(set);//未真正写入的输出流，仅仅在内存中
            writer.flush();//写入输出流，真正将数据传输出去
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String readData(){

        String response = null;
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader=new BufferedReader(reader);
            response=bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }
}
