package gaoyanshan.client.download;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciacho.gys_socket.R;

import java.util.List;

import gaoyanshan.client.adapter.FileDataAdapter;

public class DownAdapter extends RecyclerView.Adapter<DownAdapter.ViewHolder> implements View.OnClickListener{
    private List<DownloadData> downloadDatas;
    private DownAdapter.OnItemClickListener mOnItemClickListener = null;

    public DownAdapter(List<DownloadData> downloadDatas) {
        this.downloadDatas = downloadDatas;
    }
    public interface OnItemClickListener {
        void onClick(View view, int position);
    };
    public void setOnItemClickListener(DownAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onClick(view,(int)view.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView fileName_text;
        ProgressBar progressBar;
        TextView downloadText;
        CheckBox checkBox;
        public ViewHolder(View view){
            super(view);
            fileName_text=view.findViewById(R.id.download_item_text);
            progressBar=view.findViewById(R.id.download_item_progress);
            downloadText=view.findViewById(R.id.download_text);
            checkBox=view.findViewById(R.id.download_checkbox);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_download_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DownloadData downloadData=downloadDatas.get(position);
        holder.fileName_text.setText(downloadData.getFileName());
        holder.progressBar.setProgress(downloadData.getProgress());
        holder.downloadText.setText(downloadData.getProgress()+"%");
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
               downloadData.setChecked(isChecked);
            }
        });
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return downloadDatas.size();
    }


}
