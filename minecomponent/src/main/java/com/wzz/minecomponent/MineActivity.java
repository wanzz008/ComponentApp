package com.wzz.minecomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wzz.commonlib.AppConfig;
import com.wzz.commonlib.EventUtils;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
    }

    public void goToLogin(View view) {
        EventUtils.open( this, AppConfig.COMPONENTS2[0] );
    }
}
