package com.example.hu.a20180126;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ShowMessageActivity extends AppCompatActivity {

    public Message message;
    private TextView title;
    private RecyclerView recyclerView;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        title = (TextView)findViewById(R.id.title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        back = (Button)(Button)findViewById(R.id.back);

        String tag,phoneNumber;
        tag = getIntent().getStringExtra("tag");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        List<Message> messages;
        if(tag==null){
            messages = DataSupport.where("phoneNumber = ?",phoneNumber).find(Message.class);
        }
        else{
            messages = DataSupport.where("tag = ? and phoneNumber = ?",tag,phoneNumber).find(Message.class);
        }
        Log.d("mydebug", "onCreate: is messages is null? "+String.valueOf(messages==null));

        MessageAdapter adapter = new MessageAdapter(messages,tag,this);
        Log.d("mydebug", "onCreate: adapter is null "+String.valueOf(adapter==null));
        Log.d("mydebug", "onCreate: recyclerView==null? "+String.valueOf(recyclerView==null));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            String tag = data.getStringExtra("tag");
            if(message!=null){
                message.setTag(tag);
                message.save();
            }
          //  setResult(2);
            finish();
        }
    }
}
