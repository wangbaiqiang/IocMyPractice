package com.wbq.iocmypractice.utils;

import android.app.Activity;
import android.view.View;

import com.wbq.iocmypractice.annotation.ContentView;
import com.wbq.iocmypractice.annotation.EventBase;
import com.wbq.iocmypractice.annotation.ViewInject;
import com.wbq.iocmypractice.proxy.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：${wbq} on 2016/12/2 14:54
 * 邮箱：wangbaiqiang@heigo.com.cn
 */

public class InjectUtils {
    public static void inject(Activity activity){
            //注入布局
        injectLayout(activity);
        //注入视图
        injectViews(activity);
        //注入事件
        injectEvents(activity);
    }

    /**
     * 注入事件 onClick
     * @param activity
     */
    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        //getMethod获得公有的方法 下面是获得公有和私有。。。方法都获得到
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method:methods){
            //获取方法上所有的注解
            Annotation[] onClickAnnotations = method.getAnnotations();
            for (Annotation annotation:onClickAnnotations){
                //下面两种方法
               /* Class<? extends Annotation> aClass = annotation.getClass();
                EventBase eventBase = aClass.getAnnotation(EventBase.class);*/
                Class<? extends Annotation> annotationType = annotation.annotationType();
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if(eventBase==null) {
                    continue;
                }
                //获取事件的三个必须要素，进行事件的注入
                Class<?> listenerType = eventBase.listenerType();
                String listenerSetter = eventBase.listenerSetter();
                String methodName = eventBase.methodName();
                //方法的对应关系
                Map<String,Method> methodMap=new HashMap<>();
                methodMap.put(methodName,method);
                //获取btn等控件的实例 才能注入
                //这里的annotationType就是onClickAnnotation的Class
                try {
                    Method valueMethod=annotationType.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    for (int id:viewIds){
                        View view=activity.findViewById(id);
                        //设置事件监听 难点 我们在这里怎么去设置监听呢？
                        //首先要拿到setOnClickListener这个方法
                        if(view==null) {
                            continue;
                        }
                        Method setOnClickListener = view.getClass().getMethod(listenerSetter, listenerType);
                        //那么这个方法要怎么执行 获得对象有很多方法
                        //1.我们new但是我们这里肯定不是
                        //2.通过contrustuct.的反射newInstance  这里也不行 因为这里是一个接口
                        //3.那么我们怎么实现呢  就是我们常用的动态代理的实现 下面我们来实现吧

                        ListenerInvocationHandler invocationHandler=new ListenerInvocationHandler(activity,methodMap);
                        //运行时，jvm自动生成listenerType接口的代理对象 核心：控制访问
                        Object proxyInstance = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, invocationHandler);
                        setOnClickListener.invoke(view,proxyInstance);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注入views控件等 即视图
     * @param activity
     */
    private static void injectViews(Activity activity) {
        Class<?> clazz = activity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field:declaredFields){
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if(viewInject!=null) {
                int id = viewInject.value();
                View view = activity.findViewById(id);
                try {
                    //只有public你才不用设置这句 其他private project。。。的属性要设置否则报错
                    field.setAccessible(true);
                    field.set(activity,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注入布局
     * @param activity
     */
    private static void injectLayout(Activity activity) {
        Class<?> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        int layout=contentView.value();
        activity.setContentView(layout);
    }
}
