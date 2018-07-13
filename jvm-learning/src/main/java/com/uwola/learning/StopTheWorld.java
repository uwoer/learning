package com.uwola.learning;

import java.util.HashMap;

/**
 *  -Xmx512M -Xms512M -XX:+UseSerialGC -Xloggc:gc.log -XX:+PrintGCDetails
 * Created by uwoer on 2018/7/13.
 */
public class StopTheWorld {
    /**
     *  模拟工作线程 每0.1秒打印一次
     */
    public static class PrintThread extends Thread{
        public static final long starttime=System.currentTimeMillis();
        @Override
        public void run(){
            try{
                while(true){
                    long t=System.currentTimeMillis()-starttime;
                    System.out.println("time:"+t);
                    Thread.sleep(100);
                }
            }catch(Exception e){

            }
        }
    }

    /**
     *  模拟垃圾回收线程
     */
    public static class MyThread extends Thread{
        HashMap<Long,byte[]> map=new HashMap<Long,byte[]>();
        @Override
        public void run(){
            try{
                while(true){

                    if(map.size()*512/1024/1024>=400){
                        System.out.println("=====准备清理=====:"+map.size());
                        map.clear();
                    }

                    for(int i=0;i<1024;i++){
                        map.put(System.nanoTime(), new byte[512]);
                    }
                    Thread.sleep(1);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        PrintThread thread = new PrintThread();
        thread.start();

        MyThread myThread = new MyThread();
        myThread.start();
    }
}
