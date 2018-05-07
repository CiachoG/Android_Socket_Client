package gaoyanshan.client.operator;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import gaoyanshan.client.socket.ClientSocket;
import gaoyanshan.client.adapter.NavAdapter;

/**
 * Created by Ciacho on 2018/5/4.
 */

public class NavRecOperator {
    private RecyclerView mRecyclerView;
    private Context context;
    private Handler handler;
    private SendMsgOperator sendMsgOperator;
    public NavRecOperator(RecyclerView mRecyclerView, Context context, Handler handler) {
        this.mRecyclerView = mRecyclerView;
        this.context = context;
        this.handler = handler;
        sendMsgOperator=new SendMsgOperator(handler,context);
    }
    public void updateRecycleView(final List<String> mRData){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        NavAdapter m_RecycleAdapter = new NavAdapter(mRData);
        mRecyclerView.setAdapter(m_RecycleAdapter);
        m_RecycleAdapter.setOnItemClickListener(new NavAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String str ="";
                for(int i=0;i<position;i++)
                {
                    str=str+mRData.get(i)+"\\";
                }
                str=str+mRData.get(position)+"\\";
                sendMsgOperator.dir(str);
            }
        });
        m_RecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mRData.size() - 1);
    }
}
