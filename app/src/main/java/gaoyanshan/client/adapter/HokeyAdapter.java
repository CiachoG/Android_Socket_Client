package gaoyanshan.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciacho.gys_socket.R;

import java.util.ArrayList;

import gaoyanshan.client.data.HotKeyData;
import gaoyanshan.client.data.PPT_HotKey;

/**
 * Created by Ciacho on 2018/5/6.
 */

public class HokeyAdapter extends RecyclerView.Adapter<HokeyAdapter.ViewHoder> {

    ArrayList<HotKeyData> hotKeyDataArrayList=new ArrayList<>();

    public HokeyAdapter(String type)
    {
        if(type.equals("ppt")){
            hotKeyDataArrayList=new PPT_HotKey().getHotkeyList();
        }
    }
    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return hotKeyDataArrayList.size();
    }

    static class ViewHoder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHoder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.hotkey_image);
            textView=itemView.findViewById(R.id.hotkey_text);
        }
    }

}
