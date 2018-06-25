package gaoyanshan.client.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import java.io.File;
import java.util.ArrayList;

import gaoyanshan.client.Dialog.LoginDialog;
import gaoyanshan.client.Dialog.MousePadDialog;
import gaoyanshan.client.Dialog.HotkeyDialog;
import gaoyanshan.client.adapter.FileDataAdapter;
import gaoyanshan.client.data.FileData;
import gaoyanshan.client.operator.SendMsgOperator;
import gaoyanshan.client.upload.UpLoadActivity;
import gaoyanshan.client.view.ShowRemoteFileHandler;

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
                LoginDialog loginDialog=new LoginDialog(this);
                mHandler=new ShowRemoteFileHandler(MyActivity.this,mRecyclerView,netFileDataList,mRecyclerView2);
                loginDialog.setmHandler(mHandler).show();
                break;
            case R.id.menu_mousepad:
                if(mHandler!=null&& getSharedPreferences("ipAndPort",MODE_PRIVATE).getString("ip","")!=""){
                    MousePadDialog mousePadDialog=new MousePadDialog(MyActivity.this);
                    mousePadDialog.setmHandler(mHandler);
                    mousePadDialog.show();
                }
                else {
                    Toast.makeText(this,"请先连接电脑",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_upload:
                if(mHandler!=null&& getSharedPreferences("ipAndPort",MODE_PRIVATE).getString("ip","")!="")
                {
                    if(netFileDataList.get(0).getLevel()==1)
                    {
                        Toast.makeText(this,"此处不支持上传",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent= new Intent(this, UpLoadActivity.class);
                        startActivityForResult(intent,1);
                    }
                }
                else {
                    Toast.makeText(this,"请先连接电脑",Toast.LENGTH_SHORT).show();
                }
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
