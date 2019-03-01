package com.wzz.commonlib;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/3/1.
 */

public class EventUtils {

    /**
     * 页面跳转
     * @param context
     * @param className
     */
    public static void open(Context context , String className ){
        try {
            Class<?> clazz = Class.forName(className);
            Intent intent = new Intent(context,clazz);
            context.startActivity( intent );
        } catch (ClassNotFoundException e) {
            Toast.makeText(context , "未集成，无法跳转" ,Toast.LENGTH_SHORT).show();
            Log.e("wzz---","未集成，无法跳转");
            e.printStackTrace();
        }
    }

    /**
     * 页面跳转，可以传参，参数放在intent中，所以需要传入一个intent
     * @param context
     * @param className
     * @param intent
     */
    public static void open(Context context , String className , Intent intent ){
        try {
            Class<?> clazz = Class.forName(className);
            intent.setClass( context , clazz );
            context.startActivity( intent );
        } catch (ClassNotFoundException e) {
            Toast.makeText(context , "未集成，无法跳转" ,Toast.LENGTH_SHORT).show();
            Log.e("wzz---","未集成，无法跳转");
            e.printStackTrace();
        }
    }
}
