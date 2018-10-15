package com.uwola.learning;

import java.util.concurrent.ConcurrentHashMap;

/**
 * jdk 中的二进制操作
 * Created by uwoer on 2018/10/9.
 */
public class Binary {

    public static void main(String[] args) {
        // 判断奇偶
        // 只要根据最未位是0还是1来决定，为0就是偶数，为1就是奇数。
        // 因此可以用if ((a & 1) == 0)代替if (a % 2 == 0)来判断a是不是偶数。
        // a为偶数时 a & 1 为0
        // a为奇数时 a & 1 为1
        for (int i = 0; i < 100; ++i){
            if ((i & 1)==0 ){
                System.out.println(i);
            }

        }

        for (int i = 0; i < 100; ++i){
            if ((i & 1)==1 ){
                System.out.println(i);
            }

        }

        System.out.println(Integer.toBinaryString(-5));

        // 2个数不借助于第3个数进行互换
        int a =13;
        int b = 6;
        a ^= b;
        b ^= a;
        a ^= b;
        System.out.println(a);
        System.out.println(b);

        // 取一个数负数
        System.out.println((~10)+1);
        System.out.println((~-11)+1);
        System.out.println((~11));
        System.out.println((~-11));
        System.out.println();

        System.out.println(my_abs(10));
        System.out.println(my_abs(-10));

        System.out.println(my_abs2(10));
        System.out.println(my_abs2(-10));

        System.out.println(my_abs2(10999));
        System.out.println(my_abs2(-111111));

        System.out.println(my_abs3((short) 10));
        System.out.println(my_abs3((short) -11));

        System.out.println(1 << 16);
        System.out.println(tableSizeFor(8));
        System.out.println(0x7fffffff);
        System.out.println(Integer.MAX_VALUE);
        indexFor(5,100);

    }

    /**
     * 32位数据取绝对值
     */
    public static  int my_abs(int a){
        // 通过移位来取符号位，int i = a >> 31;  要注意如果a为正数，i等于0; 为负数，i等于-1
        int i = a >> 31;
        return i == 0 ? a : (~a + 1);
    }

    /**
     * 32位数据取绝对值
     */
    public static  int my_abs2(int a){
        // 通过移位来取符号位，int i = a >> 31;要注意如果a为正数，i等于0，为负数，i等于-1
        int i = a >> 31;
        System.out.println("i===>"+i);
        // 对于任何数，与0异或都会保持不变
        // 对于任何数，与-1异或 就相当于取反 当i为-1时 (a ^ i) - i 即为 ~a + 1
        return ((a ^ i) - i);
    }

    /**
     * 8位数据取绝对值   short
     */
    public static  int my_abs3(short a){
        // short 为8位  除去符号位一共有7位 故需要位移7位
        // 通过移位来取符号位，int i = a >> 7;要注意如果a为正数，i等于0，为负数，i等于-1
        int i = a >> 7;
        System.out.println("i===>"+i);
        return ((a ^ i) - i);
    }

    /**
     * 64位数据取绝对值   short
     */
    public static long my_abs4(long a){
        // long 为64位  除去符号位一共有63位 故需要位移63位
        // 通过移位来取符号位，int i = a >> 63;要注意如果a为正数，i等于0，为负数，i等于-1
        int i = (int) (a >> 63);
        System.out.println("i===>"+i);
        return ((a ^ i) - i);
    }

    /**
     *  见 ConcurrentHashMap 的tableSizeFor()方法
     * 返回大于等于count的最小的2的幂次方
     */
    private static final int tableSizeFor(int count) {
        int n = count - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        System.out.println("tableSizeFor====>"+(n + 1));
        return n + 1;
    }

    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     *
     * @param n HashMap 或者 ConcurrentHashMap的数组的长度 这个值必须为2的x次幂+1
     * @param hash  hash值   获取算法：(h = key.hashCode()) ^ (h >>> 16)
     */
    private static void indexFor(int n,int hash){
        // 由于n这个值必须为2的x次幂 所以n-1的二进制形式是类似于00001111这种连续的0和连续的1的形式
        // 这个时候 (n - 1) & hash 得到的值只能是 0 到 n-1 之间
        // 这个运算的效果和 hash％(n-1)的最终效果一致   但是&运算比%运算更高效
        int index = (n - 1) & hash;
        System.out.println(index);
    }

}
