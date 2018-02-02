package com.example.hu.a20180126;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
    private float mtextWidth[];//字符串所占据的矩形宽度
    private float mtextHeight[];//字符串所占据的矩形高度
    private float maxD;//最大直径
    private  float maxnum;
    private float currentWidth,currentHeight;
    private static TextPaint textPaint;
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
        textPaint = new TextPaint();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mwidth = dm.widthPixels;
        mheight = dm.heightPixels;
        mstring=new ArrayList<>();
        mwordSize=35;
        maxnum=4;
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
        mx = new float[mstring.size()];
        my = new float[mstring.size()];
        mtextHeight = new float[mstring.size()];
        mtextWidth = new float[mstring.size()];
        Log.d("MyView","two");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("CustomView:","three");
        currentHeight=50;
        currentWidth=50;
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(mwordSize);
        textPaint.setAntiAlias(true);
        mpaint.setTextSize(mwordSize);
        for(int i=0;i<mstring.size();i++)
        {
            mnum[i] = mstring.get(i).length();
            int num = (int)mnum[i];
            int count = 1;
            while(num>maxnum){
                num /= 2;
                count++;
            }
            Log.d("mydebug", "onDraw: count = "+String.valueOf(count));
            //宽度限制放宽一个汉字的宽度（两个英文字符）
            mtextWidth[i] = mpaint.measureText(mstring.get(i))/count+mpaint.measureText("  ");
            Log.d("mydebug", "onDraw: mtextWidth[i] = "+String.valueOf(mtextWidth[i]));
            mtextHeight[i] = (textPaint.getFontMetrics().bottom-textPaint.getFontMetrics().top)*count;
            Log.d("mydebug", "onDraw: mtextHeight[i] = "+String.valueOf(mtextHeight[i]));
            mr[i] = Math.max(mtextHeight[i],mtextWidth[i])*0.8F;
            Log.d("mydebug", "onDraw: mr[i] = "+String.valueOf(mr[i]));
            if(maxD<mr[i]*2)
            {
                    maxD=mr[i]*2;
            }
        }
        for(int i=0;i<mstring.size();i++)
        {
            if(currentWidth+mr[i]*2+50>mwidth)
            {
                currentWidth=50;
                currentHeight+=maxD+50;
            }
            mpaint.setColor(Color.GRAY);
            canvas.drawCircle(currentWidth+mr[i],currentHeight+mr[i],mr[i],mpaint);
            mx[i] = currentWidth+mr[i];
            my[i] = currentHeight+mr[i];
            StaticLayout layout = new StaticLayout(mstring.get(i), textPaint, (int) mtextWidth[i] , Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            canvas.save();
            canvas.translate(currentWidth+mr[i]-mtextWidth[i]/2,currentHeight+mr[i]-mtextHeight[i]/2);
            layout.draw(canvas);
            canvas.restore();
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
