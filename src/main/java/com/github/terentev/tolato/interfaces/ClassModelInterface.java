package com.github.terentev.tolato.interfaces;

import com.github.terentev.tolato.classes.Range;
import com.github.terentev.tolato.classes.TypeData;

public interface ClassModelInterface<T> {
    Range[] tags();

    TypeData typeData(T obj, int tag);

    FieldWriterInterface<T> writer();

    FieldReaderInterface<T> reader();
}