package com.marchuck;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Target(value = TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
}