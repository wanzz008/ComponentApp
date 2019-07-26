package com.wzz.zujian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 每次文件添加和commit时，是先提交到本地仓库，只有push的时候，才会提交到GitHub服务器上。
 */

/**
 * 组件化参考：
 * https://blog.csdn.net/ican87/article/details/86612733
 */

public class CActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
