package com.wzz.commonlib.dn_router;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DnRouter {

    private static DnRouter dnRouter = new DnRouter();
    public static Context mContext ;

    public static Map<String,Class> routerMap = new HashMap<>();
    public static DnRouter getInstance(){
        return dnRouter ;
    }

    public void navigation( String path ){
        Class aClass = routerMap.get(path);

    }

    public static void init(Application application){

        mContext = application.getApplicationContext();
        // 遍历包名下所有的
//        Set<String> classNames = null ;
        List<String> classNames = new ArrayList<>();
        classNames.add("com.wzz.zujian.LoginRouter") ;
        classNames.add("com.wzz.zujian.MineRouter") ;

        try {
            for (String className : classNames) {

                Class<?> cl = Class.forName(className);
                // 如果包下的类 是IDNRouter的子类
                if ( IDNRouter.class.isAssignableFrom( cl )){
                    IDNRouter idnRouter = (IDNRouter) cl.newInstance();
                    idnRouter.onLoad( routerMap );
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
