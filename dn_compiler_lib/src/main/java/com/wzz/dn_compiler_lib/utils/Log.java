package com.wzz.dn_compiler_lib.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * 在自定义注解中打印log需要此类
 * 在Build中查看打印的log日志
 * 打印的message最好不要是汉字 显示出来是乱码
 */
public class Log {
    private Messager messager;

    private Log(Messager messager) {
        this.messager = messager;
    }

    public static Log newLog(Messager messager) {
        return new Log(messager);
    }

    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
