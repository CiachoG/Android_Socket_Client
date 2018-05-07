package gaoyanshan.client.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ciacho.gys_socket.R;

import java.util.List;

import gaoyanshan.client.test.NetFileData;

/**
 * Created by Ciacho on 2018/3/23.
 */

public class NetFileListAdapter extends ArrayAdapter<NetFileData> {

    private  int resouceId;
    public NetFileListAdapter(@NonNull Context context, int resource, @NonNull List<NetFileData> objects) {
        super(context, resource, objects);
        resouceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resouceId,parent,false);

        ImageView imageView=view.findViewById(R.id.image);
        TextView text1=view.findViewById(R.id.file_name);
        TextView text2=view.findViewById(R.id.file_date);
        TextView text3=view.findViewById(R.id.file_size);

        NetFileData netFileData=getItem(position);
        if(netFileData.getFileName().endsWith(":\\"))
        {
            imageView.setImageResource(R.drawable.ic_pan);
            text1.setText(netFileData.getFileName());
            text2.setText(netFileData.getFileModifiedDate());
        }
        else{
            if(position==0){
                imageView.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                imageView.setImageResource(R.drawable.back);
                text1.setTextSize(16);
                text1.setText(netFileData.getFilePath());

            }
            else {
                if(netFileData.isDirectory()==true){

                    imageView.setImageResource(R.drawable.ic_isdir);

                }
                else
                {
                    int index=netFileData.getFileName().lastIndexOf(".");
                    if(index==-1)
                    {
                        index=0;
                    }
                    String Suffix =netFileData.getFileName().substring(index,netFileData.getFileName().length()).toLowerCase();
                    //Log.e("Suffix:",Suffix);
                    if(Suffix.equals(".jpg"))
                    {
                        imageView.setImageResource(R.drawable.ic_pic);

                    }
                    else if(Suffix.equals(".pdf")){
                        imageView.setImageResource(R.drawable.ic_pdf);
                    }
                    else if(Suffix.equals(".txt")){
                        imageView.setImageResource(R.drawable.txt);
                    }
                    else if(Suffix.equals(".zip")||Suffix.equals(".rar")){
                        imageView.setImageResource(R.drawable.ic_zip);
                    }
                    else if(Suffix.equals(".ppt")){
                        imageView.setImageResource(R.drawable.ic_ppt);
                    }
                    else if(Suffix.equals(".doc")||Suffix.equals(".docx")){
                        imageView.setImageResource(R.drawable.ic_word);
                    }
                    else if(Suffix.equals(".xlsx")){
                        imageView.setImageResource(R.drawable.ic_excel);
                    }
                    else
                        imageView.setImageResource(R.drawable.ic_file);
                    text3.setText(netFileData.getFileSizeStr());
                }
                text1.setText(netFileData.getFileName());
                text2.setText(netFileData.getFileModifiedDate());

            }

        }
        return view;
    }
}
