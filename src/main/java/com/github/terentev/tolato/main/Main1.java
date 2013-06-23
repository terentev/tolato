package com.github.terentev.tolato.main;

import com.github.terentev.tolato.classes.Range;
import com.github.terentev.tolato.classes.TypeData;
import com.github.terentev.tolato.interfaces.*;
import com.github.terentev.tolato.serialization.TypeDataSer;
import com.google.common.collect.Lists;

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

    public static <T> void serialize(T obj, ClassModelInterface<T> model, RangeInterface ri, Towriter data, Map<Object, Integer> map) {
        FieldWriterInterface<T> fi = model.writer();
        checkState(model.tags().length != 0);
        for (Range x : model.tags()) {
            data.writeBit(true);
            ri.writeStart(x.start, data);
            ri.writeTags(x.tags, data);
        }
        for (Range x : model.tags()) {
            for (int y : x.tags) {
                TypeData td = model.typeData(obj, x.start + y);
                TypeDataSer.write(td, data);

            }
        }
    }

    public static <T> void deserialize(T obj, ClassModelInterface<T> model, RangeInterface ri, Toreader data) {
        FieldReaderInterface<T> fi = model.reader();
        List<Range> range = Lists.newArrayList();
        while (data.readBit()) {
            int start = ri.readStart(data);
            int[] tags = ri.readTags(data);
            range.add(new Range(start, tags));
        }
        for (Range x : range)
            for (int y : x.tags)
                fi.read(obj, y + x.start, data);
    }

    public static void main(String[] args) {

    }
}