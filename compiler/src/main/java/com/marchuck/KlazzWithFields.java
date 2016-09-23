package com.marchuck;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by l.marczak
 *
 * @since 9/8/16.
 */
public class KlazzWithFields {
    public final List<VariableElement> fieldsOnly = new ArrayList<>();
    public final String name;
    public final TypeElement typeElement;

    public KlazzWithFields(String name, TypeElement element) {
        this.name = name;
        this.typeElement = element;
    }
}
