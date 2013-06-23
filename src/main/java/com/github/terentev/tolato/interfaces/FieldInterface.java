package com.github.terentev.tolato.interfaces;

import com.github.terentev.tolato.classes.TypeData;

import java.util.Map;

public interface FieldInterface<T> {

    void writePrimitive(T obj, int tag, Towriter data);

    void readPrimitive(T obj, int tag, Toreader data);

    TypeData typeData(T obj, int tag, Map<Object, Integer> map);

    TypeData typeData(T obj, int tag);

    <S> S getObject(T obj, int tag);

    void setObject(T obj, int tag, Object o);

    <S> Class<S> getClass(T obj, int tag);

    int getArraySize(T obj, int tag);

    void writePrimitiveArray(T obj, int i, Towriter data);

    void readPrimitiveArray(T obj, int tag, int size, Toreader data);

    Object[] getObjectArray(T obj, int tag);

    <S> Class<S> getArrayClass(T obj, int tag);
}
