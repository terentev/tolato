package com.github.terentev.tolato.interfaces;

public interface Toreader {

    boolean readBit();

    int readBitInt();

    int[] readTags();

    int shift();
}