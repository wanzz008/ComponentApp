package com.wzz.logincomponent;

import android.app.Application;
import android.util.Log;

import com.wzz.commonlib.IAppComponent;
import com.wzz.commonlib.ServiceManager;

public class LoginApp extends Application implements IAppComponent{

    private static Application application;

    public static Application getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initialize( this );
    }


    /**
     * 如果是单独运行模式，则直接通过onCreat()初始化
     * 如果是组件开发模式，则通过main Application中的反射，调用initialize( this )方法进行初始化。
     * @param app
     */
    @Override
    public void initialize(Application app) {
        Log.d("wzz----" , "LoginApp initialize...") ;
        application = app ;
        ServiceManager.getInstance().setILoginService( new LoginService() );
    }
}
