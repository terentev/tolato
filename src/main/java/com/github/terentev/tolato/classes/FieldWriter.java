package com.github.terentev.tolato.classes;

import com.github.terentev.tolato.interfaces.FieldInterface;

import java.util.Map;

public abstract class FieldWriter<T> implements FieldInterface<T> {

    @Override
    public TypeData typeData(T obj, int tag, Map<Object, Integer> map) {
        TypeData td = this.typeData(obj, tag);
        if (td == TypeData.OBJECT && map.containsKey(getObject(obj, tag)))
            return TypeData.REFERENCE;
        return td;
    }
}