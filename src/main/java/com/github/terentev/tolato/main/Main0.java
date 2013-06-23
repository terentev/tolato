package com.github.terentev.tolato.main;

public class Main0 {

    public static void main(String[] args) {
        Integer[] a = new Integer[]{1, 2};
        Object[] h = a;
        h[0] = 1L;
        System.out.println(a[0].getClass());
    }
}