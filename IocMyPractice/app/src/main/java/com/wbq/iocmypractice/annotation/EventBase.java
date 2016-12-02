package com.wbq.iocmypractice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：${wbq} on 2016/12/2 15:40
 * 邮箱：wangbaiqiang@heigo.com.cn
 */
/**注解上的注解*/
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    Class<?> listenerType();//事件监听的类型
    String listenerSetter();//设置事件监听的方法
    String methodName();//事件执行之后执行回调的方法
}
