package com.uwola.learning;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xmx20m -Xms20m 运行代码
 * Created by uwoer on 2018/7/13.
 */
public class TraceGcTest {
    public static void main(String[] args) {
        List list = new ArrayList<byte[]>();
        while (true){
            byte[] bytes = new byte[1024];
            list.add(bytes);
        }

    }
}
