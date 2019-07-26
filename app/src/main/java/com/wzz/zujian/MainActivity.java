package com.wzz.zujian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wzz.commonlib.ServiceManager;

/**
 * 每次文件添加和commit时，是先提交到本地仓库，只有push的时候，才会提交到GitHub服务器上。
 */

/**
 * 组件化参考：
 * https://blog.csdn.net/ican87/article/details/86612733
 */

@Route(path = "/main/main")
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** 三种方式跳转：1、2 都是用反射 通过全类名找到类，调用方法 */
//                ServiceManager.getInstance().getILoginService().launch(MainActivity.this,"");

//                EventUtils.open(MainActivity.this,"com.wzz.logincomponent.LoginActivity" );

                /** 利用ARouter进行跳转 */
                ARouter.getInstance().build("/login/LoginActivity").navigation();

            }
        });


        findViewById(R.id.gozujian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceManager.getInstance().getIMineService().launch(MainActivity.this,2222);
            }
        });

//        findViewById(R.id.showUI).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                ServiceManager.getInstance().getILoginService().newUserInfoFragment(
//                        getSupportFragmentManager(),
//                        R.id.container,bundle);
//            }
//        });

    }
}
