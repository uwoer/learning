package com.uwola.learning;

/**
 * 这个问题需要研究下
 * 不同jdk版本下的表象
 */
public class TestString {

    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "abc";
        String s3 = new String("abc");
        System.out.println("s1==s2  ===>"+ s1 == s2);
        System.out.println("s1==s3  ===>"+ s1 == s3);
        System.out.println("s1==s3.intern()  ===>" + s1 == s3.intern());
    }
}
