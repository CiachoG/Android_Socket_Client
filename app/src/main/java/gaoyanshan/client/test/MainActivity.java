package gaoyanshan.client.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private  EditText editText1,editText2;
    private Button bt,bt1,bt2;
    private Handler mHandler;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylayout);
        editText1=findViewById(R.id.ip_text);
        editText2=findViewById(R.id.port_text);
        bt=findViewById(R.id.Button);
        bt1=findViewById(R.id.Button1);
        bt2=findViewById(R.id.Button2);
        listView=findViewById(R.id.listview);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectByPath("dir:c:");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectByPath("dir:d:");
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectByPath("conn:");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NetFileData item = (NetFileData) listView.getAdapter().getItem(i);
                if(item.getFilePath().split("//").length==1 && i==0){
                    Toast.makeText(MainActivity.this,"已经是最外层目录了",Toast.LENGTH_SHORT).show();
                }
                else if(i==0  ){
                    String frontFilePath = null;
                    int k=item.getFilePath().substring(0,item.getFilePath().length()-2).lastIndexOf("//");
                    frontFilePath=item.getFilePath().substring(0,k);
                    ConnectByPath(frontFilePath);

                }
                else if(item.isDirectory()==true)
                    ConnectByPath(item.getFilePath()+item.getFileName());
                else
                {
                    ConnectByPath("open:"+item.getFilePath().substring(item.getFilePath().indexOf(":")+1)+item.getFileName());

                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void ConnectByPath(String path){
        String ip=editText1.getText().toString();
        if(ipRegex(ip,editText2.getText().toString())){
            int port=Integer.parseInt(editText2.getText().toString());
        }
        else
        {
            Toast.makeText(MainActivity.this,"请输入正确格式的IP或PORT",Toast.LENGTH_SHORT).show();
        };
    }
    public  boolean ipRegex(String ip,String port) {
        Pattern p = Pattern.compile("^((25[0-5]|2[0-4]\\d|[1]{1}\\d{1}\\d{1}|[1-9]{1}\\d{1}|\\d{1})($|(?!\\.$)\\.)){4}$");
        Matcher m = p.matcher(ip);
        if(m.matches()==true && port.length()==4)
            return true;
        else
            return  false;

    }
}
