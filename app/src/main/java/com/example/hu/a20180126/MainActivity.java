package com.example.hu.a20180126;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
    private List<String> data;
    private CustomView customView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initScreen();
        bind();
        //判断数据库是否已经创建
        List<Message> messages = DataSupport.findAll(Message.class);
        //若第一次打开应用
        if(messages.size()==0){
            //动态申请权限
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},1);
            }else{
                initDatabase();//初始化数据库
            }
        }
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
            activityTitle = "请输入标签。。";
            title.setText(activityTitle);
            submit.setText("确认");
        }
        //将所有的tag按照出现的次数保存在list中
        List<String> data = getTagList();
        customView.addView(data);
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
                    Toast.makeText(this,"无权限读取短信，请开放权限",Toast.LENGTH_SHORT).show();
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
                startActivityForResult(intent,1);
                break;
            case R.id.submit:
                if(input.getText().length()==0)
                {
                    Toast.makeText(this,"标签名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(activityTitle=="请输入标签。。") {
                    Intent ret = new Intent();
                    ret.putExtra("tag", input.getText().toString());
                    setResult(2, ret);
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
        title = (TextView)findViewById(R.id.title);
        customView = (CustomView)findViewById(R.id.custom_view);
        customView.setOnClickListener(new CustomView.OnTagClickListener() {
            @Override
            public void onClick(String tag) {
                input.setText(tag);
                input.setSelection(input.getText().toString().length());
            }
        });
        back.setOnClickListener(this);
        createNew.setOnClickListener(this);
        submit.setOnClickListener(this);

    }
    //读取系统短信数据库并保存到本应用数据库中
    // TODO: 2018/2/2 这里最好修改一下只讲接受到的信息存入系统数据库？还是在应用的数据库中再添加一个字段表示是发送的短信还是接受的短信
    private void initDatabase()
    {
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


    //去掉标题栏
    private void initScreen(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            customView.addView(getTagList());
        }
    }
    private List<String> getTagList(){
        List<Message> messages = DataSupport.where("tag != ?","").find(Message.class);
        Map<String,Integer> map = new TreeMap<>();
        for(Message m:messages){
            if(!map.containsKey(m.getTag())){
                map.put(m.getTag(),1);
            }
            else{
                int t = map.get(m.getTag());
                map.put(m.getTag(),t+1);
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
        return data;
    }
}
