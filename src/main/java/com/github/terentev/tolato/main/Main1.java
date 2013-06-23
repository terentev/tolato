package com.github.terentev.tolato.main;

import com.github.terentev.tolato.classes.Range;
import com.github.terentev.tolato.classes.TypeData;
import com.github.terentev.tolato.interfaces.ClassModelInterface;
import com.github.terentev.tolato.interfaces.FieldInterface;
import com.github.terentev.tolato.interfaces.Toreader;
import com.github.terentev.tolato.interfaces.Towriter;
import com.github.terentev.tolato.serialization.PrimitiveSer;
import com.github.terentev.tolato.serialization.TypeDataSer;
import com.github.terentev.tolato.serialization.VarIntSer;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class Main1 {

    public static interface RangeInterface {
        void writeStart(int start, Towriter data);

        void writeTags(int[] tags, Towriter data);

        int readStart(Toreader data);

        int[] readTags(Toreader data);
    }

    public static class ClassModelCreator {
        public static <T> ClassModelInterface<T> get(Class<T> clazz) {
            return null;
        }
    }

    public static RangeInterface ri = null;

    public static <T> void serialize(T obj, ClassModelInterface<T> model, Towriter data, Map<Object, Integer> map) {
        FieldInterface<T> fi = model.field();
        checkState(model.tags().length != 0);
        for (Range x : model.tags()) {
            data.writeBit(true);
            ri.writeStart(x.start, data);
            ri.writeTags(x.tags, data);
        }
        for (Range x : model.tags()) {
            for (int y : x.tags) {
                int tag = x.start + y;
                TypeData td = fi.typeData(obj, tag, map);
                TypeDataSer.write(td, data);
                switch (td) {
                    case NULL:
                        break;
                    case VAR_INT:
                        fi.writePrimitive(obj, tag, data);
                        break;
                    case OBJECT: {
                        Object o = fi.getObject(obj, tag);
                        map.put(o, data.shift());
                        serialize(o, ClassModelCreator.get(fi.getClass(obj, tag)), data, map);
                        break;
                    }
                    case REFERENCE:
                        int shift = map.get(fi.getObject(obj, tag));
                        PrimitiveSer.write(-(shift - data.shift()), data);
                        break;
                    case ARRAY_PRIMITIVE:
                        VarIntSer.write(fi.getArraySize(obj, tag), data);
                        fi.writePrimitiveArray(obj, tag, data);
                        break;
                    case ARRAY_OBJECT:
                        VarIntSer.write(fi.getArraySize(obj, tag), data);
                        for (Object o : fi.getObjectArray(obj, tag)) {
                            map.put(o, data.shift());
                            serialize(o, ClassModelCreator.get(fi.getClass(obj, tag)), data, map);
                        }
                        break;
                }
            }
        }
    }

    public static <T> void deserialize(T obj, ClassModelInterface<T> model, Toreader data, Map<Integer, Object> map) throws IllegalAccessException, InstantiationException {
        FieldInterface<T> fi = model.field();
        List<Range> range = Lists.newArrayList();
        while (data.readBit()) {
            int start = ri.readStart(data);
            int[] tags = ri.readTags(data);
            range.add(new Range(start, tags));
        }
        for (Range x : range) {
            for (int y : x.tags) {
                int tag = x.start + y;
                TypeData td = TypeDataSer.read(data);
                switch (td) {
                    case NULL: {
                        throw new RuntimeException("never");
                    }
                    case VAR_INT: {
                        fi.readPrimitive(obj, tag, data);
                        break;
                    }
                    case OBJECT: {
                        Class<Object> clazz = fi.getClass(obj, tag);
                        Object o = clazz.newInstance();
                        map.put(data.shift(), o);
                        deserialize(o, ClassModelCreator.get(clazz), data, map);
                        break;
                    }
                    case REFERENCE: {
                        int before = data.shift();
                        int value = PrimitiveSer.readInt(data);
                        checkState(map.containsKey(before - value));
                        Object o = map.get(before - value);
                        fi.setObject(obj, tag, o);
                        break;
                    }
                    case ARRAY_PRIMITIVE: {
                        int size = VarIntSer.readInt(data);
                        fi.readPrimitiveArray(obj, tag, size, data);
                        break;
                    }
                    case ARRAY_OBJECT: {
                        int size = VarIntSer.readInt(data);
                        Class<Object> clazz = fi.getArrayClass(obj, tag);
                        Object[] ar = ObjectArrays.newArray(clazz, size);
                        for (int i = 0; i < size; i++) {
                            Object o = clazz.newInstance();
                            map.put(data.shift(), o);
                            deserialize(o, ClassModelCreator.get(clazz), data, map);
                            ar[i] = o;
                        }
                        fi.setObject(obj, tag, ar);
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}