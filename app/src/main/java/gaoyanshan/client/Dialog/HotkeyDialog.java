package gaoyanshan.client.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import gaoyanshan.client.adapter.HokeyAdapter;


public class HotkeyDialog extends Dialog {
    private Context context;
    private Button bt1;
    private RecyclerView mRecyclerView;
    private HokeyAdapter hokeyAdapter;
    private String type;
    private GridLayoutManager gridLayoutManager;
    private Handler handler;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public HotkeyDialog(Context context) {
        super(context);
        this.context=context;
    }

    public HotkeyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HotkeyDialog(Context context, boolean cancelable
            , OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.layout_dialog,null);
        setContentView(view);
        initSizeOfWindow();
        iniView(view);


    }
    public void initSizeOfWindow(){
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.85); // 高度设置为屏幕的0.6
        lp.height=(int)(d.heightPixels*0.6);
        dialogWindow.setAttributes(lp);

    }
    public void iniView(View view){
        bt1=view.findViewById(R.id.btn_cancel);
        mRecyclerView=view.findViewById(R.id.dialog_recyclerview);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        gridLayoutManager=new GridLayoutManager(getOwnerActivity(),2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        hokeyAdapter=new HokeyAdapter(getContext(),handler,type);
        mRecyclerView.setAdapter(hokeyAdapter);
    }

}
