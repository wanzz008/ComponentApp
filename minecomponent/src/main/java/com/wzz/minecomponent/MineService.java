package com.wzz.minecomponent;

import android.content.Context;
import android.content.Intent;

import com.wzz.commonlib.service.IMineService;

/**
 * Created by Administrator on 2019/2/27.
 */

public class MineService implements IMineService {
    @Override
    public void launch(Context context, int userId) {
        Intent intent = new Intent(context, MineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ID", userId);
        context.startActivity(intent);
    }
}
