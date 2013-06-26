package com.github.terentev.tolato.classes;

import com.github.terentev.tolato.interfaces.Towriter;

import java.util.BitSet;

public class TowriterImpl implements Towriter {
    public BitSet a = new BitSet();

    @Override
    public void writeBit(boolean value) {
        a.set(a.length(), value);
    }

    @Override
    public void writeBytes(byte[] b) {
        BitSet bit = BitSet.valueOf(b);
        for (int i = 0; i < bit.length(); i++)
            a.set(a.length(), bit.get(i));
    }

    @Override
    public int shift() {
        return a.length();
    }

}