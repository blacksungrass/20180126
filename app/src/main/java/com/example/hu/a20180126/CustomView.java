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

/**
 * Created by MSI on 2018/1/26.
 */
// TODO: 2018/2/2 还要写一个点击的setOnclickListener 来将点击的自动填到输入框里面
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
    private  float maxnum;
    private float mleveleachwords[];
    private float currentWidth,currentHeight;
    private static TextPaint textPaint;
    private OnTagClickListener mListener;
    public static interface OnTagClickListener{
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
        mstring=new ArrayList<String>();
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
        mleveleachwords=new float[mstring.size()];
        Log.d("MyView","two");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("CustomView:","three");


        currentHeight=50;
        currentWidth=50;
        mpaint.setTextSize(mwordSize);
        for(int i=0;i<mstring.size();i++)
        {
            mnum[i]=mstring.get(i).length();
            //mlengthOfWords[i]=mwordSize*mnum[i];
            //长度这样求会有问题，中英文表现不一致，中文还好，英文在圈圈内会偏左，所以我改成用Paint类自带的函数来求字符串长度了
           if(mnum[i]>maxnum)
                { //大于maxnum个字就换行
                    int num=(int)mnum[i];
                    int count=1;
                    do{
                        num=num/2;
                        count++;
                    }
                    while(num>maxnum);

                    mleveleachwords[i]=count;
                mlengthOfWords[i] = mpaint.measureText(mstring.get(i))/mleveleachwords[i];
                    mr[i]=mlengthOfWords[i]*3/4;
                }
           else{
               mlengthOfWords[i]=mpaint.measureText(mstring.get(i));
               mr[i]=mlengthOfWords[i]*2/3;

           }

            if(maxD<mr[i]*2)
            {
                maxD=mr[i]*2;
            }

        }
     //   Toast.makeText(getContext(), mstring.get(0)+"length is:"+mnum[0], Toast.LENGTH_LONG).show();

        for(int i=0;i<mstring.size();i++)
        {

            if(currentWidth+mr[i]*2+50>=mwidth)
            {

                currentWidth=50;
                // TODO: 2018/2/2  为什么还要加mr[i]*2？
                // TODO: 2018/2/2  手抖了下,还是有些问题，继续优化
                currentHeight+=maxD+50;
            }
            mpaint.setColor(Color.GRAY);
            canvas.drawCircle(currentWidth+mr[i]+mleveleachwords[i]*5,currentHeight+mr[i]+mleveleachwords[i]*5,mr[i]+mleveleachwords[i]*5,mpaint);
            mx[i] = currentWidth+mr[i];
            my[i] = currentHeight+mr[i];
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(mwordSize);
           textPaint.setAntiAlias(true);

            if(mleveleachwords[i]!=0) {

                StaticLayout layout = new StaticLayout(mstring.get(i), textPaint, (int) mlengthOfWords[i] , Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();
                canvas.translate(currentWidth+mr[i]-(mlengthOfWords[i]/3),currentHeight+(mr[i]-(mleveleachwords[i]*mwordSize*2/3)));//从100，100开始画
                layout.draw(canvas);
                canvas.restore();//别忘了restore
            }
            else{
                StaticLayout layout = new StaticLayout(mstring.get(i), textPaint, (int) mlengthOfWords[i], Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();
                canvas.translate(currentWidth+mr[i]-(mlengthOfWords[i]/2),currentHeight+(mr[i]-mwordSize*2/3));//从100，100开始画
                layout.draw(canvas);
                canvas.restore();//别忘了restore
            }
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
