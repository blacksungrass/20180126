package com.example.hu.a20180126;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private String activityTitle;//这个活动的标题
    private TextView title;
    private Button back,createNew,submit;
    private EditText input;
    private RecyclerView recyclerView;
    private List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        //判断数据库是否已经创建
        List<Message> messages = DataSupport.findAll(Message.class);
        //动态申请权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS},1);
        }else{
            if(messages.size()==0){
                initDatabase();
            }
        }



        TagAdapter adapter;

        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        //判断这个活动是否由其他活动启动
        if(from==null){
            activityTitle = "标签";
            title.setText(activityTitle);
            submit.setText("搜索");

        }
        else {
            List<String> data = intent.getStringArrayListExtra("tags");
            createNew.setVisibility(View.INVISIBLE);
            createNew.setEnabled(false);
            activityTitle = "输入标签。。";
            title.setText(activityTitle);
            submit.setText("确认");
        }
       // DataSupport.deleteAll(Message.class);
        messages = DataSupport.where("tag != ?","").find(Message.class);
        Map<String,Integer> map = new TreeMap<>();
        for(Message m:messages){
            if(!map.containsKey(m.tag)){
                map.put(m.tag,1);
            }
            else{
                int t = map.get(m.tag);
                map.put(m.tag,t+1);
            }
        }
        List<Map.Entry<String,Integer>> t = new ArrayList<>();
        t.addAll(map.entrySet());
        Collections.sort(t,new Comparator<Map.Entry<String,Integer>>(){
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        data = new ArrayList<>();
        for(Map.Entry<String,Integer> i:t){
            data.add(i.getKey());
        }

        CustomView.addView(data);

/*
        adapter = new TagAdapter(data,input);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));*/


    }

    @Override
    protected void onRestart() {
        super.onRestart();

       //CustomView.addView(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode){
                case 1:
                    if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        initDatabase();
                    }
                    else{
                        finish();
                    }
            }
        }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.create_new:
                Intent intent = new Intent(this,SelectContactsActivity.class);
                Set<String> set = new TreeSet<>();
                List<Message> messages = DataSupport.findAll(Message.class);
                for(Message i:messages){
                    set.add(i.getPhoneNumber());
                }
                List<String> args = new ArrayList<>();
                for(String i:set){
                    args.add(i);
                }
                intent.putExtra("data",(Serializable)args);
                startActivity(intent);
                break;
            case R.id.submit:
                if(input.getText().length()==0)
                {
                    Toast.makeText(this,"标签名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(activityTitle=="输入标签。。") {
                    Intent ret = new Intent();
                    ret.putExtra("tag", input.getText().toString());
                    setResult(2, ret);
                   Message message=new Message();
                   message.setTag(input.getText().toString());
                    finish();
                }
                else{
                    List<String> arg = new ArrayList<>();
                    List<Message> t = DataSupport.where("tag = ?",input.getText().toString()).find(Message.class);
                    Set<String> s = new TreeSet<>();
                    for(Message i:t){
                        s.add(i.getPhoneNumber());
                    }
                    for(String i:s){
                        arg.add(i);
                    }
                    intent = new Intent(this,SelectContactsActivity.class);
                    intent.putExtra("data",(Serializable)arg);
                    intent.putExtra("tag",input.getText().toString());
                    startActivity(intent);
                }
                break;
        }
    }

    private void bind(){
        back = (Button)findViewById(R.id.back);
        createNew = (Button)findViewById(R.id.create_new);
        submit = (Button)findViewById(R.id.submit);
        input = (EditText)findViewById(R.id.input);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        title = (TextView)findViewById(R.id.title);

        back.setOnClickListener(this);
        createNew.setOnClickListener(this);
        submit.setOnClickListener(this);
    }
    private void initDatabase()
    {
        //--------------------------------------------------------------------------------
        ContentResolver cr = getContentResolver();
        Cursor cur  = cr.query(Uri.parse("content://sms/"),null,null,null,null);
        while(cur.moveToNext())
        {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndex("body"));
            Message message = new Message();
            message.setPhoneNumber(address);
            message.setContent(body);
            message.save();
        }
        cur.close();
    }
}
