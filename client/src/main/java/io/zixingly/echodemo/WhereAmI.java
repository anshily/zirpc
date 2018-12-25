package io.zixingly.echodemo;

import io.zixingly.annotation.ZiRpcClient;

import java.lang.reflect.Method;

public class WhereAmI {
    public static void main(String[] args){
        CallTest callTest = new CallTest();

        Method[] methods = CallTest.class.getMethods();
        System.out.println(CallTest.class.getName());

        try
        {
            for (Method method : methods)
            {
                // 如果此方法有注解，就把注解里面的数据赋值到user对象
                if (method.isAnnotationPresent(ZiRpcClient.class))
                {
//                    method.getName();
//                    method.getParameterTypes();

                    System.out.println("method name:"+method.getName());
//                    method.getTypeParameters();
//                    ZiRpcClient init = method.getAnnotation(ZiRpcClient.class);
//                    method.invoke()
//                    method.invoke(user, init.value());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
//            return null;
        }

        System.out.println(callTest.call());
//        callTest.call();
    }
}
