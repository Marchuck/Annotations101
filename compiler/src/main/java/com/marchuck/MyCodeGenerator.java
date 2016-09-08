package com.marchuck;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

final class MyCodeGenerator {

    public static TypeSpec generateClass(Messager messeger, TypeElement klazz) {

        String realmieKlazzName = klazz.getSimpleName().toString();
        String newClassName = "Mutable" + realmieKlazzName;

        TypeSpec.Builder classBuilder = classBuilder(newClassName)
                .addJavadoc("hehehehehehehe javaDoc tak bardzo rozjebany")
                .addModifiers(PUBLIC, FINAL);

        boolean shouldBeImmutable = klazz.getAnnotation(RealmBean.class).mutable();

        classBuilder.addMethod(createConstructor(klazz));

        for (Element element : klazz.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD) {
                TypeMirror mirror = element.asType();
                String fieldName = element.getSimpleName().toString();
                classBuilder.addField(FieldSpec.builder(TypeName.get(mirror), fieldName, PRIVATE, FINAL).build());
            }
        }
        return classBuilder.build();
    }

    private static MethodSpec createConstructor(TypeElement klazz) {
        TypeMirror realmieMirror = klazz.asType();
        TypeName realmieType = TypeName.get(realmieMirror); //Bean as type
        String realmieKlazzName = klazz.getSimpleName().toString(); //"Bean"
        String realmieParameterName = realmieKlazzName.toLowerCase(); //bean
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(PUBLIC);

        for (Element element : klazz.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD) {
                String fieldName = element.getSimpleName().toString();

                constructorBuilder
                        .addStatement("this." + fieldName + " = " + realmieParameterName + ".get" + withFixedLetter(fieldName) + "()");
            }
        }

        constructorBuilder
                .addParameter(ParameterSpec.builder(realmieType, realmieParameterName, FINAL)
                        .build());
        return constructorBuilder.build();
    }

    private static String withFixedLetter(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
    }

}
