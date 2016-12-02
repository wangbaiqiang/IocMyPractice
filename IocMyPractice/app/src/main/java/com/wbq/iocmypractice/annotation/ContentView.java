package com.wbq.iocmypractice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：${wbq} on 2016/12/1 17:39
 * 邮箱：wangbaiqiang@heigo.com.cn
 */
//注解在运行期被使用
@Retention(RetentionPolicy.RUNTIME)
//注解使用在类上
@Target(ElementType.TYPE)

public @interface ContentView {
    int value();
}
