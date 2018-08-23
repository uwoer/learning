package com.uwola.learning;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * -XX:+PrintGCDetails -Xmx20m -Xms20m -XX:+PrintTenuringDistribution 运行代码
 *  -XX:+PrintTenuringDistribution https://my.oschina.net/go4it/blog/1628795
 *  打印对象年龄 这里Desired survivor size这行下面并没有各个age对象的分布，
 *  那就表示此次gc之后，当前survivor区域并没有age小于max threshold的存活对象。
 *  而这里一个都没有输出，表示此次gc回收对象之后，没有存活的对象可以拷贝到新的survivor区。
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
