package com.github.terentev.tolato.interfaces;

import com.github.terentev.tolato.classes.Range;

public interface ClassModelInterface<T> {
    Range[] tags();

    FieldInterface<T> field();
}