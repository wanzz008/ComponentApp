package com.wzz.zujian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wzz.commonlib.ServiceManager;

/**
 * 每次文件添加和commit时，是先提交到本地仓库，只有push的时候，才会提交到GitHub服务器上。
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceManager.getInstance().getILoginService().launch(MainActivity.this,"");
            }
        });


        findViewById(R.id.gozujian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceManager.getInstance().getIMineService().launch(MainActivity.this,2222);
            }
        });

        findViewById(R.id.showUI).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ServiceManager.getInstance().getILoginService().newUserInfoFragment(
                        getSupportFragmentManager(),
                        R.id.container,bundle);
            }
        });

    }
}
