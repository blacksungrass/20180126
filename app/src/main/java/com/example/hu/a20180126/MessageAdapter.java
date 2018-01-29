package com.example.hu.a20180126;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hu on 2018/1/29.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;
    private String tag;
    private ShowMessageActivity activity;

    class ViewHolder extends  RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView)itemView.findViewById(R.id.tv_name);
            textView2=(TextView)itemView.findViewById(R.id.tv_split);

        }
    }

    public MessageAdapter(List<Message> m,String t,ShowMessageActivity s) {
        Log.d("mydebug", "MessageAdapter: "+String.valueOf(m.size())+String.valueOf(tag==null));
        messages = m;
        tag = t;
        activity = s;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView1.setText(messages.get(position).getContent());
        if(tag!=null)
            return;
        holder.textView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,MainActivity.class);
                activity.message = messages.get(position);
                intent.putExtra("from","ShowMessageActivity");
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
