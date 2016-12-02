package com.wbq.iocmypractice.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者：${wbq} on 2016/12/2 10:16
 * 邮箱：wangbaiqiang@heigo.com.cn
 */

public class Test {
    public static void main(String[] args) {
        User u1=new User();
        u1.setId(10);

        User u2=new User();
        u2.setUserName("pq");

        User u3=new User();
        u3.setEmail("1036607309@qq.com");
        //思考 字段名要和数据库的名进行映射
        String sql1=query(u1);
        String sql2=query(u2);
        String sql3=query(u3);
    }

    private static String query(User u) {
        StringBuilder sb=new StringBuilder();
        Class c= u.getClass();
        boolean isTableExist = c.isAnnotationPresent(Table.class);
        if(!isTableExist) {
            return null;
        }else{
            Table t = (Table) c.getAnnotation(Table.class);
            String tableName=t.value();
            sb.append("select * from ").append(tableName).append(" where 1=1");//为了防止没有任何条件报错
            //遍历所有的字段
            Field[] fields = c.getDeclaredFields();
            for (Field f:fields){
                //处理每个字段对应的sql
                //拿到字段名
                boolean isFExist = f.isAnnotationPresent(Column.class);
                if(!isFExist) {
                    //说明是普通的字段不需要处理
                    continue;
                }else{
                    Column column = f.getAnnotation(Column.class);
                    String columnName=column.value();
                    //拿到字段的值 拼接sql
                    String fieldName=f.getName();
                    String getMehodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                    Object fieldVaue=null;
                    try {
                        Method method = c.getMethod(getMehodName);
                        fieldVaue = method.invoke(u,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //拼接sql
                    if(fieldVaue==null||fieldVaue instanceof Integer&&(Integer)fieldVaue==0) {
                        continue;
                    }
                    sb.append(" and ").append(fieldName);
                    if(fieldVaue instanceof String) {
                        sb.append("=").append("'").append(fieldVaue).append("'");
                    }else if(fieldVaue instanceof Integer){
                        sb.append("=").append(fieldVaue);
                    }
                    sb.append(" and ").append(columnName).append("=").append(fieldVaue);
                }
            }
        }

        return sb.toString();
    }
}
