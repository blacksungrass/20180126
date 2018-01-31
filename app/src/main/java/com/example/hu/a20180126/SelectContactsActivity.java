package com.example.hu.a20180126;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BlockedNumberContract.BlockedNumbers.COLUMN_ID;
import static android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME;

public class SelectContactsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SelectContactsActivity";
    private RecyclerView recyclerView;
    private Button back;
    private List<String> rawData;
    private  List<String> data;
    private  List<String> rdata;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        initScreen();
        bind();
        String tag = getIntent().getStringExtra("tag");
      //从前一个活动传来的原始信息,号码保证无重复
        rawData = (List<String>)getIntent().getSerializableExtra("data");

        data = new ArrayList<>();
        rdata = new ArrayList<>();

        //动态申请权限
        if(ContextCompat.checkSelfPermission(SelectContactsActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SelectContactsActivity.this,new String[]{Manifest.permission.READ_CONTACTS},2);
        }else{
            for(String i:rawData){
                String t =getDisplayNameByNumber(i);
                if(!"".equals(t)){
                    data.add(t);
                    rdata.add(i);
                }
            }
        }


        recyclerView.setAdapter(new ContectAdapter(data,rdata,this,tag));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void bind()
    {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(this);


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 2:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    for(String i:rawData){
                        String t =getDisplayNameByNumber(i);
                        if(!"".equals(t)){
                            data.add(t);
                            rdata.add(i);
                        }
                    }
                }
                else{
                    finish();
                }
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    public String getDisplayNameByNumber(String phoneNum) {
        String contactName = "";
        ContentResolver cr = getContentResolver();
        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                new String[] { phoneNum }, null);
        if (pCur.moveToFirst()) {
            contactName = pCur .getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            pCur.close();
        }
        return contactName;
    }

    private void initScreen(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
