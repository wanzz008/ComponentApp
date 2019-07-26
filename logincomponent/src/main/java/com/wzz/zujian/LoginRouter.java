package com.wzz.zujian;

import android.util.Log;

import com.wzz.commonlib.dn_router.IDNRouter;
import com.wzz.logincomponent.LoginActivity;

import java.util.Map;

public class LoginRouter implements IDNRouter {
    @Override
    public void onLoad(Map<String, Class> map) {
        map.put("login/main", LoginActivity.class);
        Log.i( "wzz-----", "onLoad: LoginRouter");
    }
}
