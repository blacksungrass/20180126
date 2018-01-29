package com.example.hu.a20180126;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hu on 2018/1/29.
 */

public class ContectAdapter extends RecyclerView.Adapter<ContectAdapter.ViewHolder> {

    private List<String> list1,list2;
    private SelectContactsActivity activity;
    private String tag;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView)itemView.findViewById(R.id.tv_name);
            textView2=(TextView)itemView.findViewById(R.id.tv_split);
        }
    }


    public ContectAdapter(List<String> list1,List<String> list2,SelectContactsActivity a,String t) {
        this.list1 = list1;//人名
        this.list2 = list2;//号码
        activity = a;
        tag = t;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView1.setText(list1.get(position));
        if(tag==null){
            holder.textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,ShowMessageActivity.class);
                    intent.putExtra("phoneNumber",list2.get(position));
                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.startActivityForResult(intent,1);
                    activity.finish();
                }
            });
        }
        else{
            holder.textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,ShowMessageActivity.class);
                    intent.putExtra("phoneNumber",list2.get(position));
                    intent.putExtra("tag",tag);
                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.startActivityForResult(intent,2);
                    activity.finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }
}
