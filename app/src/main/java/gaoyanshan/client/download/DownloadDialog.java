package gaoyanshan.client.download;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import java.util.ArrayList;
import java.util.List;

import gaoyanshan.client.operator.SendMsgOperator;

public class DownloadDialog extends Dialog{
    private Context context;
    private Button bt1,bt2,bt3;
    private RecyclerView recyclerView;
    private List<DownloadData> downloadDataList=new ArrayList<>();
    private DownAdapter downAdapter;
    private List<DownloadTask> downloadTaskList=new ArrayList<>();

    public DownloadDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    private DownloadListener listener = new DownloadListener() {

        @Override
        public void onProgress(int port, int progress) {
                for(int i=0;i<downloadTaskList.size();i++){
                   if( downloadDataList.get(i).getPort().equals(String.valueOf(port))){
                       downloadDataList.get(i).setProgress(progress);
                       downAdapter.notifyDataSetChanged();
                   }
                }
        }

        @Override
        public void onSuccess() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.layout_download_dialog,null);
        setContentView(view);
        initSizeOfWindow();
        initView(view);

    }
    public void initSizeOfWindow(){
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        lp.height=(int)(d.heightPixels*0.8);
        dialogWindow.setAttributes(lp);

    }
    public void  initView(View view)
    {
        bt1=view.findViewById(R.id.download_background);
        bt2=view.findViewById(R.id.download_pause);
        bt3=view.findViewById(R.id.download_cancel);
        recyclerView=view.findViewById(R.id.download_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        downAdapter=new DownAdapter(downloadDataList);
        recyclerView.setAdapter(downAdapter);
        downAdapter.setOnItemClickListener(new DownAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(),""+position,Toast.LENGTH_SHORT).show();

            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<downloadTaskList.size();i++){
                    DownloadData downloadData=downloadDataList.get(i);
                    if(downloadData.isPause() && downloadData.isChecked())
                    {
                        DownloadTask downloadTask=new DownloadTask(listener);
                        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,downloadData.getIp(),downloadData.getPort(),downloadData.getFileName(),downloadData.getDownloadLen(),
                                String.valueOf(i));
                        downloadData.setPause(false);
                        downloadTaskList.set(i,downloadTask);

                    }
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"暂停",Toast.LENGTH_SHORT).show();
                for(int i=0;i<downloadTaskList.size();i++){
                    if(downloadDataList.get(i).isChecked() && downloadDataList.get(i).isPause()==false)
                    {
                        Toast.makeText(getContext(),i+"",Toast.LENGTH_SHORT).show();

                        downloadDataList.get(i).setPause(true);
                        downloadTaskList.get(i).pauseDownload();
                    }
                }
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<downloadTaskList.size();i++){
                    DownloadTask  downloadTask=downloadTaskList.get(i);
                    if(downloadDataList.get(i).isChecked())
                    {
                        downloadTask.cancelDownload();
                        downloadDataList.remove(i);
                        downloadTask=null;
                        downloadTaskList.remove(i);
                        downAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    public DownloadDialog startDownload(DownloadData downloadData)
    {
        downloadDataList.add(downloadData);
        DownloadTask downloadTask=new DownloadTask(listener);
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,downloadData.getIp(),downloadData.getPort(),downloadData.getFileName(),downloadData.getDownloadLen(),
                                String.valueOf(downloadDataList.size()-1));
        downloadTaskList.add(downloadTask);
        return this;
    }
    public DownloadDialog updateView(List<DownloadData> downloadData)
    {
        this.downloadDataList=downloadData;
        downAdapter.notifyDataSetChanged();
        return this;
    }

}