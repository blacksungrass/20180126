package com.example.hu.a20180126;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hu on 2018/1/29.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<String> tags;
    private EditText input;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView;
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    TextView obj = (TextView)v;
                    input.setText(obj.getText());
                }
            });
        }
    }

    public TagAdapter(List<String> t,EditText editText) {
        tags = t;
        input = editText;
        CustomView.addView(tags);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
