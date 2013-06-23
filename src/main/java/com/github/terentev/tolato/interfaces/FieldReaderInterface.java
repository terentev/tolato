package com.github.terentev.tolato.interfaces;

public interface FieldReaderInterface<T> {
    void read(T obj, int tag, Toreader tw);
}