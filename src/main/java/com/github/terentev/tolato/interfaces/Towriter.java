package com.github.terentev.tolato.interfaces;

public interface Towriter {
    void writeBit(boolean value);

    void writeBytes(byte[] b);

    int shift();
}