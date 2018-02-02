package com.example.hu.a20180126;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// TODO: 2018/2/2 要考虑下如果tag太多，level太多以至于页面不够长，放不下的情况
// TODO: 2018/2/2 要考虑tag非常长的情况下应如何表现
public class CustomView extends View {
    private Paint mpaint;
    private float mwidth;
    private float mheight;
    private List<String> mstring;
    private float mwordSize;//字的大小
    private float mnum[];//tag字符串字数
    private float mlengthOfWords[];//tag长度
    private float mr[];//半径
    private float mx[];//X坐标
    private float my[];//Y坐标
    private float maxD;//最大直径
    private float mcurrentLevel;
    private OnTagClickListener mListener;
    public interface OnTagClickListener{
        void onClick(String tag);
    }
    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            for(int i=0;i<mstring.size();i++){
                float xx = Math.abs(x-mx[i]);
                float yy = Math.abs(y-my[i]);
                if(Math.pow(xx,2)+Math.pow(yy,2)<=mr[i]*mr[i]){
                    if(mListener!=null)
                        mListener.onClick(mstring.get(i));
                    break;
                }
            }
        }
        return true;
    }

    public CustomView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        mpaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mwidth = dm.widthPixels;
        mheight = dm.heightPixels;
        mstring=new ArrayList<>();
        mwordSize=35;
        mcurrentLevel=1;
        Log.d("MyView","one");

    }

    public void setOnClickListener(OnTagClickListener listener){
        mListener = listener;
    }

    public void addView(List<String> string){
        //我也不确定要不要加这个。。。
        invalidate();
        mstring.clear();
        for(int i=string.size()-1;i>=0;i--)
           mstring.add(string.get(i));
        mnum = new float[mstring.size()];
        mlengthOfWords = new float[mstring.size()];
        mr = new float[mstring.size()];
        maxD = 0;
        mx = new float[string.size()];
        my = new float[string.size()];
        Log.d("MyView","two");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("CustomView:","three");
        mpaint.setTextSize(mwordSize);
        for(int i=0;i<mstring.size();i++)
        {
            mnum[i]=mstring.get(i).length();
            //mlengthOfWords[i]=mwordSize*mnum[i];
            //长度这样求会有问题，中英文表现不一致，中文还好，英文在圈圈内会偏左，所以我改成用Paint类自带的函数来求字符串长度了
            mlengthOfWords[i] = mpaint.measureText(mstring.get(i));
            mr[i]=mlengthOfWords[i]*2/3;
            if(maxD<mr[i]*2)
            {
                maxD=mr[i]*2;
            }

        }
        float currentWidth=50;
        float currentHeight=50;
        for(int i=0;i<mstring.size();i++)
        {

            if(currentWidth+mr[i]*2+50>=mwidth)
            {
                mcurrentLevel++;
                currentWidth=50;
                // TODO: 2018/2/2  为什么还要加mr[i]*2？ 
                currentHeight+=mr[i]*2+maxD+50;
            }
            mpaint.setColor(Color.GRAY);
            canvas.drawCircle(currentWidth+mr[i],currentHeight+mr[i],mr[i],mpaint);
            mx[i] = currentWidth+mr[i];
            my[i] = currentHeight+mr[i];
            mpaint.setColor(Color.BLACK);

            canvas.drawText(mstring.get(i),currentWidth+mr[i]-(mlengthOfWords[i]/2),currentHeight+mr[i]+(mwordSize*1/3),mpaint);
            currentWidth+=mr[i]*2+50;

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
