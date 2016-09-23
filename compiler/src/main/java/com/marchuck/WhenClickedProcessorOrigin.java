package com.marchuck;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import static com.marchuck.Utils.getPackageName;
import static com.squareup.javapoet.JavaFile.builder;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class WhenClickedProcessorOrigin extends AbstractProcessor {

    private static final String ANNOTATION = "@" + WhenClicked.class.getSimpleName();

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        return singleton(WhenClicked.class.getCanonicalName());
        return new HashSet<>();
//        return new HashSet<>(Arrays.asList(WhenClicked.class.getCanonicalName(), Injectable.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ArrayList<AnnotatedClass> annotatedClasses = new ArrayList<>();
        ArrayList<ExecutableElement> clickableMethods = new ArrayList<>();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Injectable.class)) {
            TypeElement annotatedClass = (TypeElement) annotatedElement;
            try {
                annotatedClasses.add(buildAnnotatedClass(annotatedClass));
            } catch (NoPackageNameException | IOException e) {
                e.printStackTrace();
            }
//            messager.printMessage(Diagnostic.Kind.NOTE, "processing next injectable " + annotatedClass.getSimpleName().toString()
//                            + "\ngetEnclosedElements\n" + printCollection(annotatedClass.getEnclosedElements())
//                            + "\n" + (annotatedClass.getEnclosingElement())
//                    , annotatedClass);
        }
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(WhenClicked.class)) {
            // Our annotation is defined with @Target(value=TYPE). Therefore, we can assume that
            // this annotatedElement is a TypeElement.
            //TypeElement annotatedClass = (TypeElement) annotatedElement;
            if (annotatedElement.getKind() == ElementKind.METHOD) {
                ExecutableElement nextMethod = (ExecutableElement) annotatedElement;
                clickableMethods.add(nextMethod);

                messager.printMessage(Diagnostic.Kind.NOTE, "processing next injectable " + nextMethod.getSimpleName().toString()
                        + "\ngetEnclosedElements\n" + printCollection(nextMethod.getEnclosedElements())
                        + "\n" + (nextMethod.getTypeParameters()), nextMethod);
            }
        }
        try {
            generate(annotatedClasses, clickableMethods);
        } catch (Exception e) {
            messager.printMessage(ERROR, "Couldn't generate class " + e.getMessage());
        }
        return true;
    }

    private boolean isValidClass(TypeElement annotatedClass) {

        if (!ClassValidator.isPublic(annotatedClass)) {
            String message = String.format("Classes annotated with %s must be public.",
                    ANNOTATION);
            messager.printMessage(ERROR, message, annotatedClass);
            return false;
        }

        if (ClassValidator.isAbstract(annotatedClass)) {
            String message = String.format("Classes annotated with %s must not be abstract.",
                    ANNOTATION);
            messager.printMessage(ERROR, message, annotatedClass);
            return false;
        }
        return true;
    }

    private AnnotatedClass buildAnnotatedClass(TypeElement annotatedClass)
            throws NoPackageNameException, IOException {
        ArrayList<String> variableNames = new ArrayList<>();
        for (Element element : annotatedClass.getEnclosedElements()) {
            if (!(element instanceof VariableElement)) {
                continue;
            }
            VariableElement variableElement = (VariableElement) element;
            variableNames.add(variableElement.getSimpleName().toString());
        }
        return new AnnotatedClass(annotatedClass, variableNames);
    }

    private void generate(List<AnnotatedClass> annos, ArrayList<ExecutableElement> annotatedMethods)
            throws NoPackageNameException, IOException {
        if (annos.size() == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, "WhenClicked processing end");
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "WhenClicked annotations size  == " + annos.size());
            String packageName = getPackageName(processingEnv.getElementUtils(), annos.get(0).typeElement);
            TypeSpec generatedClass = MyCodeGenerator.generateClass(messager, annos, annotatedMethods);
            if (generatedClass == null) return;
            JavaFile javaFile = builder(packageName, generatedClass).build();

            javaFile.writeTo(processingEnv.getFiler());
        }
    }

    public static <T> String printCollection(Collection<T> a) {
        if (a == null)
            return "null";

        int iMax = a.size() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');

        Iterator<T> iterator = a.iterator();

        for (int i = 0; ; i++) {
            b.append(String.valueOf(iterator.next().toString()));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}

