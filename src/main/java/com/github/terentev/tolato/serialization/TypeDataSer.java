package com.github.terentev.tolato.serialization;

import com.github.terentev.tolato.classes.TypeData;
import com.github.terentev.tolato.interfaces.Toreader;
import com.github.terentev.tolato.interfaces.Towriter;

public class TypeDataSer {

    public static void write(TypeData td, Towriter data) {
        switch (td) {
            case NULL:
                break;
            case VAR_INT:
                data.writeBit(false);
                break;
            case OBJECT:
                data.writeBit(true);
                data.writeBit(false);
                data.writeBit(false);
                break;
            case REFERENCE:
                data.writeBit(true);
                data.writeBit(false);
                data.writeBit(true);
                break;
            case ARRAY_OBJECT:
                data.writeBit(true);
                data.writeBit(true);
                data.writeBit(false);
                break;
            case ARRAY_PRIMITIVE:
                data.writeBit(true);
                data.writeBit(true);
                data.writeBit(true);
                break;
        }
    }

    public static TypeData read(Toreader data) {
        int a = data.readBit() ? 0 : (1 << 2 + data.readBitInt() << 1 + data.readBitInt());
        switch (a) {
            case 0:
                return TypeData.VAR_INT;
            case 0b100:
                return TypeData.OBJECT;
            case 0b101:
                return TypeData.REFERENCE;
            case 0b110:
                return TypeData.ARRAY_OBJECT;
            case 0b111:
                return TypeData.ARRAY_PRIMITIVE;
            default:
                throw new RuntimeException("never");
        }
    }
}