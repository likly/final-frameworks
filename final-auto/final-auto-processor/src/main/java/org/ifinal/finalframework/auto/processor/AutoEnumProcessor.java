package org.ifinal.finalframework.auto.processor;


import org.ifinal.finalframework.auto.service.annotation.AutoProcessor;
import org.ifinal.finalframework.auto.service.processor.AbsServiceProcessor;
import org.ifinal.finalframework.core.annotation.IEnum;
import org.ifinal.finalframework.data.annotation.Transient;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Set;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@AutoProcessor
@SuppressWarnings("unused")
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoEnumProcessor extends AbsServiceProcessor {
    private static final String ENUM = IEnum.class.getName();
    private static final String TRANSIENT = Transient.class.getName();

    private TypeElementFilter typeElementFilter;
    private TypeElement typeElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeElement = processingEnv.getElementUtils().getTypeElement(ENUM);
        this.typeElementFilter = new TypeElementFilter(processingEnv, typeElement, processingEnv.getElementUtils().getTypeElement(TRANSIENT));
    }

    @Override
    protected boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        ElementFilter.typesIn(roundEnv.getRootElements())
                .stream()
                .filter(typeElementFilter::matches)
                .forEach(entity -> addService(typeElement, entity));


        return false;
    }


}

