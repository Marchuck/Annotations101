package com.marchuck;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by l.marczak
 *
 * @since 9/23/16.
 */
public class BuildableBuilder {
    private static final String CLASS_NAME = "BuilderXXX";

    public static TypeSpec generateClass(Messager messager, List<Element> classes) {
        TypeSpec.Builder builder = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL);
        for ( Element anno : classes) {

            messager.printMessage(Diagnostic.Kind.WARNING, "next element: " + anno.getSimpleName());
            messager.printMessage(Diagnostic.Kind.NOTE, "enclosed element: " + anno.getEnclosingElement().getSimpleName());
            messager.printMessage(Diagnostic.Kind.NOTE, "enclosing elements: " + printedCollection(anno.getEnclosedElements()));
//            builder.addMethod(makeCreateStringMethod(anno));
        }
        return builder.build();
    }

    private static String printedCollection(List<? extends Element> enclosedElements) {
        if (enclosedElements.size() == 0) return "<EMPTY>";
        StringBuilder sb = new StringBuilder()
                .append('[')
                .append(enclosedElements.get(0).getSimpleName());

        for (int j = 1; j < enclosedElements.size(); j++) {
            sb.append(", ").append(enclosedElements.get(j).getSimpleName());
        }
        return sb.append(']').toString();
    }

    /**
     * @return a createString() method that takes annotatedClass's type as an input.
     */
    private static MethodSpec makeCreateStringMethod(AnnotatedClass annotatedClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("return \"%s{\" + ", annotatedClass.annotatedClassName));
        for (String variableName : annotatedClass.variableNames) {
            builder.append(String.format(" \"%s='\" + String.valueOf(instance.%s) + \"',\" + ",
                    variableName, variableName));
        }
        builder.append("\"}\"");
        return methodBuilder("createString")
                .addJavadoc("@return string suitable for {@param instance}'s toString()")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(get(annotatedClass.getType()), "instance")
                .addStatement(builder.toString())
                .returns(String.class)
                .build();
    }
}
