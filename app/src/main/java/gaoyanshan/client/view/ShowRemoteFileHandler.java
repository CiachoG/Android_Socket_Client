package gaoyanshan.client.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import gaoyanshan.client.data.FileData;
import gaoyanshan.client.download.DownloadData;
import gaoyanshan.client.download.DownloadDialog;
import gaoyanshan.client.operator.NavRecOperator;
import gaoyanshan.client.operator.SendMsgOperator;
import gaoyanshan.client.socket.ClientSocket;
import gaoyanshan.client.adapter.FileDataAdapter;
import gaoyanshan.client.upload.UploadProgressDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ciacho on 2018/3/23.
 */

public class ShowRemoteFileHandler extends Handler {
    private  Context context;
    private RecyclerView mRecyclerView,mRecyclerView2;
    private ArrayList<FileData> netFileDataList;
    List<String> mRData = new ArrayList<String>();
    private NavRecOperator navRecOperator;
    private SendMsgOperator sendMsgOperator;
    SharedPreferences sharedPreferences;
    private DownloadDialog downloadDialog;
    public List<String> getmRData() {
        return mRData;
    }

    public  ShowRemoteFileHandler(Context context, RecyclerView mRecyclerView, ArrayList<FileData> netFileDataList, RecyclerView mRecyclerView2){
        this.context=context;
        this.mRecyclerView=mRecyclerView;
        this.mRecyclerView2=mRecyclerView2;
        sendMsgOperator=new SendMsgOperator(this,context);
        this.netFileDataList=netFileDataList;
        downloadDialog=new DownloadDialog(context);
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what)
        {
            case 0: //错误信息
                break;
            case 1://获取列表
                sharedPreferences=context.getSharedPreferences("ipAndPort",MODE_PRIVATE);

                jsonArrayBobean(msg);
                updateRecycleView();
                break;
            case 2://打开文件
                break;
            case 3://鼠标
                break;
            case 4://热键
                break;
            case 5://下载
                String downloadInfo=msg.getData().getString(ClientSocket.KEY_SERVER_ACK_MSG);
                DownloadData downloadData=new DownloadData(downloadInfo.split(":")[2],downloadInfo.split(":")[3],
                                            0,sharedPreferences.getString("ip","192.168.1.2"),downloadInfo.split(":")[1]);

                downloadDialog.show();
                downloadDialog.startDownload(downloadData);
                break;
            case 6:
                SharedPreferences sharedPreferences2=context.getSharedPreferences("path",MODE_PRIVATE);
                String uploadPath="";
                if(sharedPreferences2.getString("updownPath","")!=""){
                    uploadPath=sharedPreferences2.getString("updownPath","");
                    String uploadInfo=msg.getData().getString(ClientSocket.KEY_SERVER_ACK_MSG);
                    UploadProgressDialog uploadProgressDialog=new UploadProgressDialog(context)
                            .initParameter(sharedPreferences.getString("ip",""),uploadInfo.split(":")[1],uploadPath)
                            .starUpload();
                    uploadProgressDialog.show();
                }

                break;
            case 7://删除返回
                String delReturnMsg=msg.getData().getString(ClientSocket.KEY_SERVER_ACK_MSG);
                sendMsgOperator.dir(netFileDataList.get(0).getParentPath());
                Toast.makeText(context,delReturnMsg.split(":")[1],Toast.LENGTH_SHORT).show();
                break;

        }
    }
    public void jsonArrayBobean(Message msg){
        netFileDataList.clear();
        Bundle bundle=msg.getData();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        String fileDatastr=bundle.getString(ClientSocket.KEY_SERVER_ACK_MSG);
        JsonObject jsonObject = new JsonParser().parse(fileDatastr).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("FileData");
        for (JsonElement jsonElement : jsonArray) {
            FileData fileData = gson.fromJson(jsonElement, new TypeToken<FileData>() {}.getType());
            mRData= Arrays.asList(("此电脑\\"+fileData.getParentPath()).split("\\\\"));
            netFileDataList.add(fileData);

        }
        navRecOperator=new NavRecOperator(mRecyclerView2,context,this);
        navRecOperator.updateRecycleView(mRData);
    }
    public void updateRecycleView()
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        final FileDataAdapter fileDataAdapter=new FileDataAdapter(netFileDataList);
        mRecyclerView.setAdapter(fileDataAdapter);
        fileDataAdapter.setOnItemClickListener(new FileDataAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
               FileData fileData=netFileDataList.get(position);
                if(fileData.getFilename().equals("..."))
                {
                    sendMsgOperator.connect();
                }
                else {
                    if(fileData.getIsDirectory()==1)
                    {
                        sendMsgOperator.dir(fileData.getFilePath());
                    }
                    else {
                        Toast.makeText(context,"打开"+fileData.getFilePath(),Toast.LENGTH_SHORT).show();
                        sendMsgOperator.open(fileData.getFilePath());
                    }
                }
            }
        });
    }
}

