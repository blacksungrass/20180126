package com.example.hu.a20180126;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by hu on 2018/1/29.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;
    private String tag;
    private ShowMessageActivity activity;
    private String mMonth;

    private String mDay;

    private String mWay;
    private String mHours;
    private String mMinute;



    class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView mtimeanddate;
        private TextView mmessage;

        public ViewHolder(View itemView) {
            super(itemView);
            mmessage = (TextView)itemView.findViewById(R.id.tv_message);
            mtimeanddate=(TextView)itemView.findViewById(R.id.tv_timeanddate);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        initDate(holder);
        holder.mmessage.setText(messages.get(position).getContent());
        if(tag!=null)
            return;
        holder.mmessage.setOnClickListener(new View.OnClickListener(){
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


    private void initDate(ViewHolder mholder) {//获取系统日期


        Calendar calendar = Calendar.getInstance();


        mMonth = calendar.get(Calendar.MONTH) + 1 + "";//获取月份要+1,从0开始表示1月

        mDay = calendar.get(Calendar.DAY_OF_MONTH) + "";//获取一月中的某一天

        mWay = calendar.get(Calendar.DAY_OF_WEEK) + "";//获取星期


        //获取小时

        mHours = calendar.get(Calendar.HOUR_OF_DAY) + "";

		/*if (calendar.get(Calendar.HOUR) < 10) {

			mHours = "0" + calendar.get(Calendar.HOUR_OF_DAY);

		} else {

			mHours = calendar.get(Calendar.HOUR_OF_DAY) + "";

		}*/


        //获取分钟

        if (calendar.get(Calendar.MINUTE) < 10) {

            mMinute = "0" + calendar.get(Calendar.MINUTE);

        } else {

            mMinute = calendar.get(Calendar.MINUTE) + "";

        }


        //星期的格式化

        switch (mWay) {

            case "1":

                mWay = "天";

                break;

            case "2":

                mWay = "一";

                break;

            case "3":

                mWay = "二";

                break;

            case "4":

                mWay = "三";

                break;

            case "5":

                mWay = "四";

                break;

            case "6":

                mWay = "五";

                break;

            case "7":

                mWay = "六";

                break;

        }

        mholder.mtimeanddate.setText(mMonth+"-"+mDay+"    星期"+mWay+"" +"    "+mHours+":"+mMinute);

    }

}


