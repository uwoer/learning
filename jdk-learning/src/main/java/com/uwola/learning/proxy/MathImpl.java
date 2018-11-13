package com.uwola.learning.proxy;

/**
 *  接口实现类
 *  被代理的目标对象
 */
public class MathImpl implements IMath{


    @Override
    public int add(int n1, int n2) {
        int result = n1+n2;
        System.out.println(n1+"+"+n2+"="+result);
        return result;
    }

    @Override
    public int sub(int n1, int n2) {
        int result = n1-n2;
        System.out.println(n1+"-"+n2+"="+result);
        return result;
    }

    @Override
    public int mut(int n1, int n2) {
        int result = n1*n2;
        System.out.println(n1+"*"+n2+"="+result);
        return result;
    }

    @Override
    public int div(int n1, int n2) {
        int result = n1/n2;
        System.out.println(n1+"/"+n2+"="+result);
        return result;
    }
}
