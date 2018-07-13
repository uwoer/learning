package com.uwola.learning;

/**
 * -Xmx20m -Xms20m -Xmn2m -XX:+PrintGCDetails 运行代码
 * Created by uwoer on 2018/7/13.
 */
public class XmnTest {
    public static void main(String[] args) {
        byte[] bytes = null;
        for(int i=0;i<10;i++){
            bytes = new byte[1*1024*1024];
        }
    }
}
