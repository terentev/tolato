package com.github.terentev.tolato.serialization;

import com.github.terentev.tolato.interfaces.Toreader;
import com.github.terentev.tolato.interfaces.Towriter;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkState;


public class VarIntSer {

    public static void writeInt(int i, Towriter data) {
        byte[] a = new byte[10];
        CodedOutputStream cos = CodedOutputStream.newInstance(a);
        try {
            cos.writeSInt32NoTag(i);
            data.writeBytes(Arrays.copyOf(a, a.length - cos.spaceLeft()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByte(byte i, Towriter data) {
        writeLong(i, data);
    }

    public static void writeShort(short i, Towriter data) {
        writeLong(i, data);
    }

    public static void writeChar(char i, Towriter data) {
        writeInt(i, data);
    }

    public static void writeLong(long i, Towriter data) {
        byte[] a = new byte[10];
        CodedOutputStream cos = CodedOutputStream.newInstance(a);
        try {
            cos.writeSInt64NoTag(i);
            data.writeBytes(Arrays.copyOf(a, a.length - cos.spaceLeft()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readInt(Toreader data) {
        return Ints.checkedCast(readRawVarint64(data));
    }

    public static byte readByte(Toreader data) {
        long l = readRawVarint64(data);
        checkState(l == (byte) l);
        return (byte) l;
    }

    public static short readShort(Toreader data) {
        return Shorts.checkedCast(readRawVarint64(data));
    }

    public static char readChar(Toreader data) {
        return Chars.checkedCast(readRawVarint64(data));
    }

    public static long readLong(Toreader data) {
        return readRawVarint64(data);
    }

    public static long readRawVarint64(Toreader data) {
        int shift = 0;
        long result = 0;
        while (shift < 64) {
            final byte b = data.readByte();
            result |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
            shift += 7;
        }
        throw new RuntimeException();
    }
}