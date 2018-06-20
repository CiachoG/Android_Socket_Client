package gaoyanshan.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ciacho.gys_socket.R;

import java.util.List;


/**
 * Created by wilson on 2017/4/18.
 */

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder> implements View.OnClickListener {
    private List<String> mDatas;
    private OnItemClickListener mOnItemClickListener = null;
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onClick(view, (Integer) view.getTag());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public NavAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameText;
        public ViewHolder(View itemView) {
            super(itemView);
            fileNameText = itemView.findViewById(R.id.nav_text);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mulu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(NavAdapter.ViewHolder holder, int position) {
        String string=mDatas.get(position);
        holder.fileNameText.setText(string);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }



}
