package com.wzz.commonlib;

import com.wzz.commonlib.service.ILoginService;
import com.wzz.commonlib.service.IMineService;

/**
 * Created by Administrator on 2019/2/27.
 */

public class ServiceManager {


    private static final ServiceManager instance = new ServiceManager();

    public static ServiceManager getInstance(){return instance;}

    private ServiceManager(){

    }

    private ILoginService mILoginService;
    private IMineService mIMineService;


    public ILoginService getILoginService(){
        return mILoginService ;
    }
    public void setILoginService(ILoginService service){
        this.mILoginService = service ;
    }

    public IMineService getIMineService(){
        return mIMineService ;
    }

    public void setIMineService(IMineService service){
        this.mIMineService = service;
    }

}
