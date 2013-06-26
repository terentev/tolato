package com.github.terentev.tolato.main;

import com.github.terentev.tolato.annotation.Tag;
import com.github.terentev.tolato.classes.FieldWriter;
import com.github.terentev.tolato.classes.Range;
import com.github.terentev.tolato.classes.TowriterImpl;
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

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class Main1 {

    public static void p(Object o) {
        System.out.println(o);
    }

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

    public static RangeInterface ri = new RangeInterface() {
        @Override
        public void writeStart(int start, Towriter data) {
            if (start == 0)
                data.writeBit(false);
        }

        @Override
        public void writeTags(int[] tags, Towriter data) {
            data.writeBit(false);
            data.writeBit(false);
            data.writeBit(true);
        }

        @Override
        public int readStart(Toreader data) {
            if (!data.readBit())
                return 0;
            return 0;
        }

        @Override
        public int[] readTags(Toreader data) {
            return new int[]{0};
        }
    };

    public static class X {
        @Tag(0)
        public int a;
    }

    public static class ClassModelX implements ClassModelInterface<X> {

        @Override
        public Range[] tags() {
            return new Range[]{new Range(0, new int[]{0})};
        }

        @Override
        public FieldInterface<X> field() {
            return new FieldWriter<X>() {
                @Override
                public void writePrimitive(X obj, int tag, Towriter data) {
                    switch (tag) {
                        case 0: {
                            PrimitiveSer.writeInt(obj.a, data);
                            break;
                        }
                    }
                }

                @Override
                public void readPrimitive(X obj, int tag, Toreader data) {
                    switch (tag) {
                        case 0: {
                            obj.a = PrimitiveSer.readInt(data);
                            break;
                        }
                    }
                }

                @Override
                public TypeData typeData(X obj, int tag) {
                    switch (tag) {
                        case 0: {
                            return TypeData.VAR_INT;
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public <S> S getObject(X obj, int tag) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public void setObject(X obj, int tag, Object o) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public <S> Class<S> getClass(X obj, int tag) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public int getArraySize(X obj, int tag) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public void writePrimitiveArray(X obj, int tag, Towriter data) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public void readPrimitiveArray(X obj, int tag, int size, Toreader data) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public Object[] getObjectArray(X obj, int tag) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }

                @Override
                public <S> Class<S> getArrayClass(X obj, int tag) {
                    switch (tag) {
                        case 0: {
                            throw new RuntimeException();
                        }
                    }
                    throw new RuntimeException();
                }
            };
        }
    }

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
                        PrimitiveSer.writeInt(-(shift - data.shift()), data);
                        break;
                    case ARRAY_PRIMITIVE:
                        VarIntSer.writeInt(fi.getArraySize(obj, tag), data);
                        fi.writePrimitiveArray(obj, tag, data);
                        break;
                    case ARRAY_OBJECT:
                        VarIntSer.writeInt(fi.getArraySize(obj, tag), data);
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
        X x = new X();
        x.a = 1;
        TowriterImpl data = new TowriterImpl();
        serialize(x, new ClassModelX(), data, new IdentityHashMap<Object, Integer>());
        p(byteArrayToString(data.a.toByteArray(), ""));
        p(data.a.length());
    }

    public static String byteArrayToString(byte[] bytes, String joiner) {
        if (bytes.length == 0)
            return "";
        StringBuilder sb = new StringBuilder(bytes.length * 8 + joiner.length() * bytes.length);
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--)
                sb.append(((b & (1 << i)) == 0) ? "0" : "1");
            sb.append(joiner);
        }
        sb.setLength(sb.length() - joiner.length());
        return sb.toString();
    }
}