package com.wzz.logincomponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wzz.commonlib.AppConfig;
import com.wzz.commonlib.EventUtils;

/**
 * 我们给 Login组件 中的 LoginActivity 添加注解：@Route(path = "/login/LoginActivity" )，需要注意的是这里的路径至少需要有两级，/xx/xx，之所以这样是因为ARouter使用了路径中第一段字符串(/ /)作为分组，比如像上面的”login”，而分组这个概念就有点类似于ActivityRouter中的组件声明 @Module
 * 原文：https://blog.csdn.net/guiying712/article/details/55213884
 */


@Route(path = "/login1/TestActivity" )
public class TestActivity extends AppCompatActivity {

    public static final String EXTRA_TARGET_CLASS = "EXTRA_TARGET_CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToMine(View view) {
        EventUtils.open( this , AppConfig.COMPONENTS2[1] );
    }
}
