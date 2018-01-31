package com.example.hu.a20180126;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSI on 2018/1/26.
 */

public class CustomView extends View {
    private Paint mpaint;
    private float mwidth;
    private float mheight;
    private static List<String> mstring;
    private static float mwordSize;
    private static float mnum[];
    private static float mlengthOfWords[];
    private static float mr[];
    private static float maxD;
    private static float mcorrentLevel;


    public CustomView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        mpaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
         mwidth = dm.widthPixels;
        mheight = dm.heightPixels;
        mstring=new ArrayList<String>();
        mwordSize=35;
        mcorrentLevel=1;
        Log.d("MyView","one");

    }

    public static void addView(List<String> string){

       for(int i=string.size()-1;i>=0;i--)
           mstring.add(string.get(i));


        mnum=new float[mstring.size()];
        mlengthOfWords=new float[mstring.size()];
        mr=new float[mstring.size()];
        maxD=0;
        Log.d("MyView","two");
    }

    public void removeView(){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Log.d("CustomView:","three");
        for(int i=0;i<mstring.size();i++)
        {
            mnum[i]=mstring.get(i).length();
            mlengthOfWords[i]=mwordSize*mnum[i];
            mr[i]=mlengthOfWords[i]*2/3;
            if(maxD<mr[i]*2)
            {
                maxD=mr[i]*2;
            }

        }

        float correntWith=50;
        float correntHight=50;

        for(int i=0;i<mstring.size();i++)
        {

            if(correntWith+mr[i]*2+50>=mwidth)
            {
                mcorrentLevel++;
                correntWith=50;
                correntHight+=mr[i]*2+maxD+50;
            }
                mpaint.setColor(Color.GRAY);
                canvas.drawCircle(correntWith+mr[i],correntHight+mr[i],mr[i],mpaint);
                mpaint.setColor(Color.BLACK);
                mpaint.setTextSize(mwordSize);
                canvas.drawText(mstring.get(i),correntWith+mr[i]-(mlengthOfWords[i]/2),correntHight+mr[i]+(mwordSize*1/3),mpaint);
                correntWith+=mr[i]*2+50;

        }
    }


    public float getMwidth() {
        return mwidth;
    }

    public void setMwidth(float mwidth) {
        this.mwidth = mwidth;
    }

    public float getMheight() {
        return mheight;
    }

    public void setMheight(float mheight) {
        this.mheight = mheight;
    }


    public float getMwordSize() {
        return mwordSize;
    }

    public static void setMwordSize(float mwordsize) {
        mwordSize = mwordsize;
    }

    public float[] getMnum() {
        return mnum;
    }

    public void setMnum(float[] mnum) {
        this.mnum = mnum;
    }

    public float[] getMlengthOfWords() {
        return mlengthOfWords;
    }

    public void setMlengthOfWords(float[] mlengthOfWords) {
        this.mlengthOfWords = mlengthOfWords;
    }

    public float[] getMr() {
        return mr;
    }

    public void setMr(float[] mr) {
        this.mr = mr;
    }
}
