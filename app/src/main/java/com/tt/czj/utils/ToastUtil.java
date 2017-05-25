package com.tt.czj.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.tt.czj.R;

/**
 * Created by czj on 15-8-26.
 */
public class ToastUtil {

    private static String oldMsg;
    /**
     * The constant toast.
     */
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;

    /**
     * Show toast.
     *
     * @param context the context
     * @param s       the s
     */
    public static void showToast(Context context, String s){
        if(toast==null){
            toast =Toast.makeText(context, s, Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackgroundResource(R.color.colorBg);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER,0,-200);
            toast.show();
            oneTime=System.currentTimeMillis();
        }else{
            twoTime=System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_SHORT){
                    toast.show();
                }
            }else{
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }


    /**
     * Show toast.
     *
     * @param context the context
     * @param resId   the res id
     */
    public static void showToast(Context context, int resId){
        showToast(context, context.getString(resId));
    }

}
