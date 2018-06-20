package gaoyanshan.client.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciacho.gys_socket.R;

import java.util.List;

import gaoyanshan.client.data.FileData;

/**
 * Created by Ciacho on 2018/4/28.
 */

public class FileDataAdapter extends RecyclerView.Adapter<FileDataAdapter.ViewHolder> implements View.OnClickListener{
    private List<FileData> mFileDataList;
    private OnItemClickListener mOnItemClickListener = null;
    private int position;
    private FileData fileData;
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onClick(view,(int)view.getTag());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    };

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public FileDataAdapter(List<FileData> mFileDataList) {
        this.mFileDataList = mFileDataList;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        ImageView imageView;
        TextView fileNameText;
        TextView fileDataText;
        TextView fileSizeText;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            fileNameText=itemView.findViewById(R.id.file_name);
            fileDataText=itemView.findViewById(R.id.file_date);
            fileSizeText=itemView.findViewById(R.id.file_size);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("选择操作");
            contextMenu.add(Menu.NONE, R.id.context_menu_hotkey,
                    Menu.NONE, "热键操作");
            contextMenu.add(Menu.NONE, R.id.context_menu_download,
                    Menu.NONE, "下载");
            contextMenu.add(Menu.NONE, R.id.context_menu_delete,
                    Menu.NONE, "删除");
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FileData fileData=mFileDataList.get(position);
        if(fileData.getLevel()==1)
        {
            holder.imageView.setImageResource(R.drawable.ic_pan);
            holder.fileNameText.setText(fileData.getFilePath());
            holder.fileDataText.setText(fileData.getFileModifileDate());
        }
        else{
            holder.fileNameText.setText(fileData.getFilename());

            if(fileData.getIsDirectory()==1)
            {
                if(fileData.getFilename().equals("..") || fileData.getFilename().equals("...")){
                    holder.imageView.setImageResource(R.drawable.back);
                    holder.fileDataText.setText("");
                    holder.fileSizeText.setText("");
                }
                else
                {
                    holder.imageView.setImageResource(R.drawable.ic_isdir);
                    holder.fileDataText.setText(fileData.getFileModifileDate());
                }

            }
            else {
                holder.fileDataText.setText(fileData.getFileModifileDate());
                holder.imageView.setImageResource(setImageType(fileData.getSuffix()));
                holder.fileSizeText.setText(fileData.getFileSizeStr());
            }
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });

    }
    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
    @Override
    public int getItemCount() {
        return mFileDataList.size();
    }
    public int setImageType(String type){
        switch (type){
            case "doc":
                return R.drawable.ic_word;
            case "ppt":
                return R.drawable.ic_ppt;
            case "xlsx":
            case "xsl":
                return R.drawable.ic_excel;
            case "zip":
            case "rar":
                return R.drawable.ic_zip;
            case "png":
            case "jpg":
                return R.drawable.ic_pic;
            case "txt":
                return R.drawable.txt;
            case "mp3":
                return R.drawable.ic_mp3;
            case "mp4":
                return R.drawable.ic_mp4;
            case "wmv":
                return R.drawable.ic_wmv;
            default:
                return R.drawable.ic_file;
        }

    }

}
