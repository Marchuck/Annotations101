package com.marchuck;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeName.BOOLEAN;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.*;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

final class RealmBeanCodeGenerator {

    private static final String CLASS_NAME = "RealmBeanCodeGeneratorImpl";

    public static TypeSpec generateClass(Messager messeger, KlazzWithFields klazz) {

        /** create class {@link CLASS_NAME } and fill with fields and methods */

        TypeSpec.Builder classBuilder = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL)
                .addJavadoc("javaDoc for ");

        String annotatedInjectableClass = klazz.name;

        CodeBlock.Builder builder = CodeBlock.builder();

        for (VariableElement element : klazz.fieldsOnly) {

            builder.addStatement(element.asType().toString() + " " + element.toString());
        }
        //todo: in case of processing not only one class, we have to structurize :
        // match each Injectable with list of WhenClicked annotations and process them separately

//        ClassName hoverboard = ClassName.get(klazz.typeElement.getEnclosingElement()., "Hoverboard");
        classBuilder.addMethod(methodBuilder("get")
                .addCode(builder.build())
//                .addParameter(ParameterSpec.builder(Object.class, "scope", FINAL).build())
                //              .addStatement(scopeCasting(annotatedInjectableClass, "ref"))
                //.addCode(injectionAndListeners)
                .addStatement("return true")
                .returns(BOOLEAN)
                .build());

        return classBuilder.build();
    }

}
