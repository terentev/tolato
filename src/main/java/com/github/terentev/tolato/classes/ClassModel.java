package com.github.terentev.tolato.classes;

import com.github.terentev.tolato.interfaces.ClassModelInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClassModel<T> implements ClassModelInterface<T> {
    private static final Logger log = LoggerFactory.getLogger(ClassModel.class);

}