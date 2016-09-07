package com.marchuck;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeName.BOOLEAN;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

final class MyCodeGenerator {

    private static final String CLASS_NAME = "Injector";

    public static TypeSpec generateClass(Messager messeger, List<AnnotatedClass> classes, ArrayList<ExecutableElement> executableElements) {

        if (classes.isEmpty()) {
            messeger.printMessage(Diagnostic.Kind.ERROR, "Empty annotated classes");
            return null;
        }

        /** create class {@link CLASS_NAME } and fill with fields and methods */
        TypeSpec.Builder classBuilder = classBuilder(CLASS_NAME)
                .addJavadoc("hehehehehehehe javaDoc tak bardzo rozjebany")
                .addModifiers(PUBLIC);

        CodeBlock injectionAndListeners = injectAndSetListeners(executableElements, "ref");
        //todo: in case of processing not only one class, we have to structurize :
        // match each Injectable with list of WhenClicked annotations and process them separately
        AnnotatedClass annotatedClass = classes.get(0);

        String annotatedInjectableClass = annotatedClass.annotatedClassName;

        classBuilder.addMethod(methodBuilder("inject").addModifiers(PUBLIC, STATIC)
                .addParameter(ParameterSpec.builder(Object.class, "scope", FINAL).build())
                .addStatement(scopeCasting(annotatedInjectableClass, "ref"))
                .addCode(injectionAndListeners)
                .addStatement("return true")
                .returns(BOOLEAN)
                .build());

        return classBuilder.build();
    }

    private static String scopeCasting(String annotatedInjectableClass, String injectionScope) {
        return "final " + annotatedInjectableClass + " " + injectionScope + " = (" + annotatedInjectableClass + ") scope";
    }

    private static CodeBlock injectAndSetListeners(List<ExecutableElement> executableElements, String injectionScope) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        for (int i = 0; i < executableElements.size(); i++) {
            ExecutableElement stmt = executableElements.get(i);

            int resourceId = stmt.getAnnotation(WhenClicked.class).value();

            codeBlock.addStatement("android.view.View v_" + i + "  = " + injectionScope + ".findViewById(" + resourceId + ")");
            codeBlock.addStatement("v_" + i + ".setOnClickListener(new android.view.View.OnClickListener() {\n" +
                    "            @Override\n" +
                    "            public void onClick(android.view.View v) {\n" +
                    "                " + injectionScope + "." + stmt.getSimpleName().toString() + "();" +
                    "                \n" +
                    "            }\n" +
                    "        })");

        }
        return codeBlock.build();
    }
}
