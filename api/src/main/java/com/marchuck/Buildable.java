package com.marchuck;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by l.marczak
 *
 * @since 9/8/16.
 */
@Target(value = {
        /** Class, interface (including annotation type), or enum declaration */
        TYPE,
        /** Field declaration (includes enum constants) */
        java.lang.annotation.ElementType.FIELD,

        /** Method declaration */
        java.lang.annotation.ElementType.METHOD,

        /** Formal parameter declaration */
        java.lang.annotation.ElementType.PARAMETER,

        /** Constructor declaration */
        java.lang.annotation.ElementType.CONSTRUCTOR,

        /** Local variable declaration */
        java.lang.annotation.ElementType.LOCAL_VARIABLE,

        /** Annotation type declaration */
        java.lang.annotation.ElementType.ANNOTATION_TYPE,

        /** Package declaration */
        java.lang.annotation.ElementType.PACKAGE,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Buildable {
}
