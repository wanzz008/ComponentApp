package com.wzz.commonlib;

// 所有你认为可以被各个组件共享的资源，都可以放在common组件中。
public class AppConfig {

    public static final String[] COMPONENTS = {
        "com.wzz.logincomponent.LoginApp",
        "com.wzz.minecomponent.MineApp"
    };

    /**
     * Activity的全类名
     */
    public static final String[] COMPONENTS2 = {
            "com.wzz.logincomponent.LoginActivity",
            "com.wzz.minecomponent.MineActivity"
    };

}
