package com.wzz.zujian;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wzz.commonlib.AppConfig;
import com.wzz.commonlib.IAppComponent;

public class MainApp extends Application  implements IAppComponent{

    private static Application application;

    public static Application getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initialize(this);

    }

    /**
     * 利用反射，把各个Module中的Application进行初始化 [ 前提是module设置为了组件开发模式(loginRunAlone = true),不然]
     * @param app
     */
    @Override
    public void initialize(Application app) {

        Log.d("wzz----" , "MainApp initialize...") ;

        //         原文：https://blog.csdn.net/guiying712/article/details/55213884

        if (BuildConfig.DEBUG) {
            //一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);


        application = app ;

        /** 此处只有在组件开发模式下才会起作用，不然会找不到类，因为module单独运行时不会被加载。(java.lang.ClassNotFoundException: com.wzz.logincomponent.LoginApp)   */
        for (String component : AppConfig.COMPONENTS ){
            try {
                Class<?> clazz = Class.forName(component);
                Object object = clazz.newInstance();
                if ( object instanceof IAppComponent ){
                    ( (IAppComponent)object ).initialize( this );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
