package com.wzz.zujian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wzz.dn_annotation_lib.DRouter;

/**
 * 每次文件添加和commit时，是先提交到本地仓库，只有push的时候，才会提交到GitHub服务器上。
 */

/**
 * 组件化参考：
 * https://blog.csdn.net/ican87/article/details/86612733
 */
//@Route(path = "/main/b" , group = "test")
@DRouter(path = "/main/b")
public class BActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
