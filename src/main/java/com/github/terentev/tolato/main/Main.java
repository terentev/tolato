package com.github.terentev.tolato.main;

import java.util.Map;

public class Main {


    public static enum FieldType {
        BIT, BYTE1, BYTE2, BYTE4, BYTE8, BYTEX;
    }

    public static interface ClassModelInterface<T> {
        int[] tags();

        boolean isEmpty();

        boolean isOneTag();

        FieldWriterInterface<T> writer();

        FieldReaderInterface<T> reader();
    }

    public static interface FieldWriterInterface<T> {
        void write(T obj, int tag, Towriter tw);
    }

    public static interface FieldReaderInterface<T> {
        void read(T obj, int tag, Toreader tw);
    }

    public static class ClassModel<T> {
        private Map<Integer, FieldType> map;
    }

    public static <T> ClassModel<T> create(Class<T> clazz) {
        return null;
    }

    public static interface Towriter {
        void writeBit(int value);

        void writeTagMask(int[] tags);
    }

    public static interface Toreader {
        boolean readBit();

        int[] readTagMask();
    }

    public static <T> void serialize(T obj, ClassModelInterface<T> model, Towriter tw) {
        if (model.isEmpty())
            return;
        FieldWriterInterface<T> fw = model.writer();
        if (model.isOneTag()) {
            tw.writeBit(0);
            fw.write(obj, 0, tw);
            return;
        }
        tw.writeBit(1);
        tw.writeTagMask(model.tags());
        for (int tag : model.tags())
            fw.write(obj, tag, tw);
    }

    public static <T> void deserialize(T obj, ClassModelInterface<T> model, Toreader tr) {
        if (model.isEmpty())
            return;
        FieldReaderInterface<T> fr = model.reader();
        if (tr.readBit()) {
            fr.read(obj, 0, tr);
            return;
        }
        for (int tag : tr.readTagMask())
            fr.read(obj, tag, tr);
    }

    public static void main(String[] args) {


    }
}