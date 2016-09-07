package com.marchuck;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;


@Target(value = {TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhenClicked {
  int value() default 0;
}