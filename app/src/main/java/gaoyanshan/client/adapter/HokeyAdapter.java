package gaoyanshan.client.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ciacho.gys_socket.R;

import java.util.ArrayList;

import gaoyanshan.client.data.HotKeyData;
import gaoyanshan.client.data.Movie_HotKey;
import gaoyanshan.client.data.PPT_HotKey;
import gaoyanshan.client.operator.SendMsgOperator;

/**
 * Created by Ciacho on 2018/5/6.
 */

public class HokeyAdapter extends RecyclerView.Adapter<HokeyAdapter.ViewHoder> {

    ArrayList<HotKeyData> hotKeyDataArrayList=new ArrayList<>();
    private Handler handler;
    private Context context;
    public HokeyAdapter(Context context,Handler handler,String type)
    {
        this.context=context;
        this.handler=handler;
        hotKeyDataArrayList.clear();
        switch (type.toLowerCase()){
            case "ppt":
                hotKeyDataArrayList=new PPT_HotKey().getHotkeyList();
                break;
            case"mp4":
            case"adv":
            case "avi":
            case "wmv":
                hotKeyDataArrayList=new Movie_HotKey().getHotkeyList();
                break;

        }
    }
    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hotkey_item, parent, false);
        HokeyAdapter.ViewHoder viewHolder = new HokeyAdapter.ViewHoder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {
        final HotKeyData hotKeyData=hotKeyDataArrayList.get(position);
        holder.textView.setText(hotKeyData.getHotkeyName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendMsgOperator(handler,context).key(hotKeyData.getHotkeyCmd());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotKeyDataArrayList.size();
    }

    static class ViewHoder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHoder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.hotkey_text);
        }
    }

}
