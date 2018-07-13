package com.uwola.learning;

/**
 * 堆的分配参数demo
 * -Xmx20m -Xms5m 运行代码
 * Created by uwoer on 2018/7/13.
 */
public class XmTest {
    public static void main(String[] args) {
        //分配5M的内存空间
        byte[] bytes = new byte[5*1024*1024];
        System.out.println("Xmx===>"+Runtime.getRuntime().maxMemory()/1024.0/1024+"M");
        System.out.println("free mem===>"+Runtime.getRuntime().freeMemory()/1024.0/1024+"M");
        System.out.println("total mem===>"+Runtime.getRuntime().totalMemory()/1024.0/1024+"M");
        System.gc();
        System.out.println("回收内存后 Xmx===>"+Runtime.getRuntime().maxMemory()/1024.0/1024+"M");
        System.out.println("回收内存后 free mem===>"+Runtime.getRuntime().freeMemory()/1024.0/1024+"M");
        System.out.println("回收内存后 total mem===>"+Runtime.getRuntime().totalMemory()/1024.0/1024+"M");
    }
}
