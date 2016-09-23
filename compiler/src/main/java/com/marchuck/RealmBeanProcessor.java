package com.marchuck;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

import static com.marchuck.Utils.getPackageName;
import static com.squareup.javapoet.JavaFile.builder;
import static java.util.Collections.singleton;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

@SupportedAnnotationTypes("*")
@AutoService(Processor.class)
public class RealmBeanProcessor extends AbstractProcessor {

    private static final String ANNOTATION = "@" + RealmBean.class.getSimpleName();

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        return singleton(WhenClicked.class.getCanonicalName());
        return singleton(RealmBean.class.getCanonicalName());
//        return new HashSet<>(Arrays.asList(WhenClicked.class.getCanonicalName(), Injectable.class.getCanonicalName()));
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<KlazzWithFields> annotatedClasses = new ArrayList<>();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(RealmBean.class)) {
            TypeElement annotatedClass = (TypeElement) annotatedElement;
            try {
                messager.printMessage(NOTE, "processing " + annotatedClass.getSimpleName().toString());
                annotatedClasses.add(buildAnnotatedklazz(annotatedClass));
            } catch (NoPackageNameException | IOException e) {
                e.printStackTrace();
            }
        }
        try {
            generate(annotatedClasses);
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

    private KlazzWithFields buildAnnotatedklazz(TypeElement annotatedClass) throws NoPackageNameException, IOException {

        KlazzWithFields klazz = new KlazzWithFields(annotatedClass.getSimpleName().toString(), annotatedClass);

        for (Element element : annotatedClass.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD) {
                klazz.fieldsOnly.add((VariableElement) element);
            }
        }
        return klazz;
    }

    private com.marchuck.AnnotatedClass buildAnnotatedClass(TypeElement annotatedClass) throws NoPackageNameException, IOException {
        ArrayList<String> variableNames = new ArrayList<>();
        for (Element element : annotatedClass.getEnclosedElements()) {
            if (!(element instanceof VariableElement)) {
                continue;
            }
            VariableElement variableElement = (VariableElement) element;
            variableNames.add(variableElement.getSimpleName().toString());
        }
        return new com.marchuck.AnnotatedClass(annotatedClass, variableNames);
    }

    static int roundNumber;

    private void generate(List<KlazzWithFields> annos)
            throws com.marchuck.NoPackageNameException, IOException {
        ++roundNumber;
        if (annos.size() == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, "END OF ROUND #" + roundNumber);
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "ANNOS SIZE  == " + annos.size() + " at round " + roundNumber);
            String packageName = getPackageName(processingEnv.getElementUtils(), annos.get(0).typeElement);

            for (KlazzWithFields klazz : annos) {
                TypeSpec generatedClass = RealmBeanCodeGenerator.generateClass(messager, klazz);

                JavaFile javaFile = builder(packageName, generatedClass).build();

                javaFile.writeTo(processingEnv.getFiler());
            }
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

