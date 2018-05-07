package gaoyanshan.client.app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

import gaoyanshan.client.Dialog.MyDialog;
import gaoyanshan.client.adapter.FileDataAdapter;
import gaoyanshan.client.data.FileData;
import gaoyanshan.client.view.ShowRemoteFileHandler;
import gaoyanshan.client.socket.ClientSocket;

public class MyActivity extends AppCompatActivity {

    private EditText edit1;
    private EditText edit2;
    private String mIp;
    private int mPort;
    private RecyclerView mRecyclerView,mRecyclerView2;
    private ShowRemoteFileHandler mHandler;
    private ArrayList<FileData> netFileDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);
        mRecyclerView=findViewById(R.id.recyclerview);
        mRecyclerView2=findViewById(R.id.recycle);
        netFileDataList=new ArrayList<>();
        registerForContextMenu(mRecyclerView);

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
                Toast.makeText(this, netFileDataList.get(position).getFilename(),Toast.LENGTH_SHORT).show();
                MyDialog dialog = new MyDialog(this);
                dialog.show();
                break;
            case R.id.context_menu_read:
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
