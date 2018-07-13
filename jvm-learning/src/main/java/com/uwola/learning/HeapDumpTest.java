package com.uwola.learning;

import java.util.ArrayList;
import java.util.List;

/**
 *  jvm运行参数 -Xmx20m -Xms5m -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=d:/logs/oom.dump
 * Created by uwoer on 2018/7/13.
 */
public class HeapDumpTest {
    public static void main(String[] args) {
        List list = new ArrayList<byte[]>();
        for(int i=0;i<30;i++){
            byte[] bytes = new byte[1*1024*1024];
            list.add(bytes);
        }
    }
}
