package com.wbq.iocmypractice.proxy;

import android.app.Activity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 作者：${wbq} on 2016/12/2 16:53
 * 邮箱：wangbaiqiang@heigo.com.cn
 */

public class ListenerInvocationHandler implements InvocationHandler {
    private Activity activity;
    private Map<String,Method> methodMap;
    public ListenerInvocationHandler(Activity activity, Map<String, Method> methodMap) {
        this.activity=activity;
        this.methodMap=methodMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里的Method就是onClick()方法
        String name = method.getName();
        Method md=methodMap.get(name);
        if(md!=null) {
            //执行注解配置的方法
            return md.invoke(activity,args);
        }
        return md.invoke(proxy,args);
    }
}
