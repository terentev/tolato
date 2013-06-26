package com.github.terentev.tolato.serialization;

import com.github.terentev.tolato.interfaces.Toreader;
import com.github.terentev.tolato.interfaces.Towriter;

public class PrimitiveSer {

    public static void writeBool(boolean i, Towriter data) {
        VarIntSer.writeInt(i ? 1 : 0, data);
    }

    public static void writeByte(byte i, Towriter data) {
        VarIntSer.writeByte(i, data);
    }

    public static void writeShort(short i, Towriter data) {
        VarIntSer.writeShort(i, data);
    }

    public static void writeChar(char i, Towriter data) {
        VarIntSer.writeChar(i, data);
    }

    public static void writeInt(int i, Towriter data) {
        VarIntSer.writeInt(i, data);
    }

    public static void writeFloat(float i, Towriter data) {
        VarIntSer.writeInt(Float.floatToIntBits(i), data);
    }

    public static void writeLong(long i, Towriter data) {
        VarIntSer.writeLong(i, data);
    }

    public static void writeDouble(double i, Towriter data) {
        VarIntSer.writeLong(Double.doubleToLongBits(i), data);
    }


    public static boolean readBool(Toreader data) {
        return VarIntSer.readInt(data) == 1;
    }

    public static byte readByte(Toreader data) {
        return VarIntSer.readByte(data);
    }

    public static short readShort(Toreader data) {
        return VarIntSer.readShort(data);
    }

    public static char readChar(Toreader data) {
        return VarIntSer.readChar(data);
    }

    public static int readInt(Toreader data) {
        return VarIntSer.readInt(data);
    }

    public static float readFloat(Toreader data) {
        return Float.intBitsToFloat(VarIntSer.readInt(data));
    }

    public static long readLong(Toreader data) {
        return VarIntSer.readLong(data);
    }

    public static double readDouble(Toreader data) {
        return Double.longBitsToDouble(VarIntSer.readByte(data));
    }
}