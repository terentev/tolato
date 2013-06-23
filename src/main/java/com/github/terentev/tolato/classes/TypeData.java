package com.github.terentev.tolato.classes;

import com.github.terentev.tolato.interfaces.Toreader;
import com.github.terentev.tolato.interfaces.Towriter;

public enum TypeData {
    NULL,
    VAR_INT,
    OBJECT,
    REFERENCE,
    ARRAY_OBJECT,
    ARRAY_PRIMITIVE;

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
        int a = data.readBit() ? 0 : (1 << 3 + data.readBitInt() << 1 + data.readBitInt());
        switch (a) {
            case 0:
                return VAR_INT;
            case 0x100:
                return OBJECT;
            case 0x101:
                return REFERENCE;
            case 0x110:
                return ARRAY_OBJECT;
            case 0x111:
                return ARRAY_PRIMITIVE;
            default:
                throw new RuntimeException("never");
        }
    }
}