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
@Target(value = TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RealmBean {
}
