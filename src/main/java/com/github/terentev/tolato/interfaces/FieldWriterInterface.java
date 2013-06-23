package com.github.terentev.tolato.interfaces;

public interface FieldWriterInterface<T> {
    void write(T obj, int tag, Towriter tw);
}
