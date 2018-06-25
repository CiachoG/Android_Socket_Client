package gaoyanshan.client.operator;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import gaoyanshan.client.socket.ClientSocket;
import gaoyanshan.client.view.ShowRemoteFileHandler;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ciacho on 2018/5/4.
 */

public class SendMsgOperator {
    private Handler handler;
    private String mIp;
    private int mPort;
    private Context context;
    public SendMsgOperator(Handler handler, Context context) {
        this.handler = handler;
        this.context=context;
        SharedPreferences sharedPreferences=context.getSharedPreferences("ipAndPort",MODE_PRIVATE);
        mIp=sharedPreferences.getString("ip","");
        mPort=sharedPreferences.getInt("port",0);
    }
    public void connect()
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"conn:\n");
        clientSocket.work();
    }
    public void open(String set){

        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"open:"+set+"\n");
        clientSocket.work();
    }
    public void dir(String set){
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"dir:"+set+"\n");
        clientSocket.work();
    }
    public void key(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"key:"+set+"\n");
        clientSocket.work();
    }
    public void mov(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"mov:"+set+"\n");
        clientSocket.work();
    }
    public void clk(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"clk:"+set+"\n");
        clientSocket.work();
    }
    public void dlf(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"dlf:"+set+"\n");
        clientSocket.work();
    }
    public void ulf(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"ulf:"+set+"\n");
        clientSocket.work();
    }
    public void del(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"del:"+set+"\n");
        clientSocket.work();
    }
    public void wheel(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"wheel:"+set+"\n");
        clientSocket.work();
    }
    public void cmd(String set)
    {
        ClientSocket clientSocket=new ClientSocket(mIp,mPort,handler,"cmd:"+set+"\n");
        clientSocket.work();
    }
}
