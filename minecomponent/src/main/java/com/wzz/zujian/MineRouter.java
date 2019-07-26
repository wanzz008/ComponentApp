package com.wzz.zujian;

import android.util.Log;

import com.wzz.commonlib.dn_router.IDNRouter;
import com.wzz.minecomponent.MineActivity;

import java.util.Map;

public class MineRouter implements IDNRouter {
    @Override
    public void onLoad(Map<String, Class> map) {
        map.put("mine/main", MineActivity.class);
        Log.i( "wzz-----", "onLoad: MineRouter");
    }
}
