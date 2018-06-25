package gaoyanshan.client.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import gaoyanshan.client.socket.ClientSocket;

import static android.content.Context.MODE_PRIVATE;

public class LoginDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private Handler mHandler;
    private Button login_button;
    private AutoCompleteTextView login_ip;
    private EditText login_port;
    private String ip;
    private int port;
    public LoginDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.layout_login,null);
        setContentView(view);
        initSizeOfWindow();
        initView(view);

    }
    public void initSizeOfWindow(){
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        lp.height=(int)(d.heightPixels*0.4);
        dialogWindow.setAttributes(lp);

    }
    public void  initView(View view)
    {
        login_button=view.findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        login_ip=view.findViewById(R.id.login_ip);
        login_port=view.findViewById(R.id.login_port);
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public LoginDialog setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login_button:
                ip=login_ip.getText().toString();
                port= Integer.parseInt(login_port.getText().toString());
                SharedPreferences.Editor editor=context.getSharedPreferences("ipAndPort",MODE_PRIVATE).edit();
                editor.putString("ip",ip);
                editor.putInt("port",port);
                editor.apply();
                ClientSocket clientSocket=new ClientSocket(ip,port,mHandler,"conn:\n");
                clientSocket.work();
                dismiss();
                break;
        }
    }
}
