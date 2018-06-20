package gaoyanshan.client.app;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import java.io.File;
import java.util.ArrayList;

import gaoyanshan.client.Dialog.MousePadDialog;
import gaoyanshan.client.Dialog.HotkeyDialog;
import gaoyanshan.client.adapter.FileDataAdapter;
import gaoyanshan.client.data.FileData;
import gaoyanshan.client.download.DownloadActivity;
import gaoyanshan.client.download.DownloadService;
import gaoyanshan.client.operator.SendMsgOperator;
import gaoyanshan.client.upload.UpLoadActivity;
import gaoyanshan.client.view.ShowRemoteFileHandler;
import gaoyanshan.client.socket.ClientSocket;

public class MyActivity extends AppCompatActivity {

    private EditText edit1;
    private EditText edit2;
    private String mIp;
    private int mPort;
    private RecyclerView mRecyclerView,mRecyclerView2;
    public static ShowRemoteFileHandler mHandler;
    private ArrayList<FileData> netFileDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);
        mRecyclerView=findViewById(R.id.recyclerview);
        mRecyclerView2=findViewById(R.id.recycle);
        netFileDataList=new ArrayList<>();
        registerForContextMenu(mRecyclerView);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取到的是listView里的条目信息
        int position = -1;
        try {
            position = ((FileDataAdapter)mRecyclerView.getAdapter()).getPosition();

        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()){
            case R.id.context_menu_hotkey:
                HotkeyDialog dialog = new HotkeyDialog(this);
                dialog.setType(netFileDataList.get(position).getSuffix());
                dialog.setHandler(mHandler);
                dialog.show();
                break;
            case R.id.context_menu_download:
                new SendMsgOperator(mHandler,this).dlf(netFileDataList.get(position).getFilePath());
                break;
            case  R.id.context_menu_delete:
                new SendMsgOperator(mHandler,this).del(netFileDataList.get(position).getFilePath());
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_connect:
                final View view = View.inflate(this, R.layout.menu_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setView(view);
                builder.setTitle("连接");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit1=view.findViewById(R.id.ip_text);
                        edit2=view.findViewById(R.id.port_text);
                        mIp=edit1.getText().toString();
                        if(edit2.getText().toString()!=null)
                            mPort=Integer.parseInt(edit2.getText().toString());
                        SharedPreferences.Editor editor=getSharedPreferences("ipAndPort",MODE_PRIVATE).edit();
                        editor.putString("ip",mIp);
                        editor.putInt("port",mPort);
                        editor.apply();
                        mHandler=new ShowRemoteFileHandler(MyActivity.this,mRecyclerView,netFileDataList,mRecyclerView2);

                        ClientSocket clientSocket=new ClientSocket(mIp,mPort,mHandler,"conn:\n");
                        clientSocket.work();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.menu_mousepad:
                MousePadDialog mousePadDialog=new MousePadDialog(MyActivity.this);
                mousePadDialog.setmHandler(mHandler);
                mousePadDialog.show();
                break;
            case R.id.menu_upload:

                Intent intent= new Intent(this, UpLoadActivity.class);
                startActivityForResult(intent,1);

                break;
            default:
                break;
        }
        return true;
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            SharedPreferences sharedPreferences2=getSharedPreferences("path",MODE_PRIVATE);
            String uploadPath="";
            String filename="";
            if(sharedPreferences2!=null){
                uploadPath=sharedPreferences2.getString("updownPath","");
                filename=new File(uploadPath).getName();
            }
            if(netFileDataList.size()>=1)
                new SendMsgOperator(mHandler,this).ulf(netFileDataList.get(0).getParentPath()+"\\"+filename);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
