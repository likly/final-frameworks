package org.finalframework.coding.query;

import org.finalframework.coding.entity.EntityFactory;
import org.finalframework.coding.file.JavaSource;
import org.finalframework.coding.generator.JavaSourceGenerator;
import org.finalframework.coding.mapper.TypeHandlers;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * @author likly
 * @version 1.0
 * @date 2019-11-21 18:05:44
 * @since 1.0
 */

public abstract class AbsEntityGenerator<T extends JavaSource> extends JavaSourceGenerator<T> {
    private final TypeHandlers typeHandlers;

    public AbsEntityGenerator(ProcessingEnvironment processEnv, String targetRoute, TypeHandlers typeHandlers) {
        super(processEnv, targetRoute);
        this.typeHandlers = typeHandlers;
    }

    @Override
    protected T buildJavaSource(TypeElement typeElement) {
        String packageName = packageNameGenerator.generate(typeElement);
        QEntity entity = QEntityFactory.create(processEnv, packageName, EntityFactory.create(processEnv, typeElement), typeHandlers);
        return buildEntityJavaSource(typeElement, entity);
    }

    protected abstract T buildEntityJavaSource(TypeElement typeElement, QEntity entity);


}

