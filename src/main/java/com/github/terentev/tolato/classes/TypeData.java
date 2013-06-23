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
        if (!data.readBit()) {
            return VAR_INT;
        } else {
            boolean a = data.readBit();
            boolean b = data.readBit();
            if (!a && !b)
                return OBJECT;
            if (!a)
                return REFERENCE;
            if (!b)
                return ARRAY_OBJECT;
            return ARRAY_PRIMITIVE;
        }
    }
}