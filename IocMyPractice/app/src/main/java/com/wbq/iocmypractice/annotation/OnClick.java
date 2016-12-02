package com.wbq.iocmypractice.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：${wbq} on 2016/12/2 15:26
 * 邮箱：wangbaiqiang@heigo.com.cn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerType = View.OnClickListener.class,listenerSetter = "setOnclickListener",methodName = "onClick")
public @interface OnClick {//注入点击事件
    int[] value();
}
