package org.ifinal.finalframework.auto.data;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.Types;
import org.ifinal.finalframework.annotation.data.PrimaryKey;
import org.ifinal.finalframework.annotation.data.Reference;
import org.ifinal.finalframework.annotation.data.ReferenceMode;
import org.ifinal.finalframework.annotation.data.Version;
import org.ifinal.finalframework.auto.coding.beans.PropertyDescriptor;
import org.ifinal.finalframework.auto.coding.utils.Annotations;
import org.springframework.data.util.Lazy;
import org.springframework.data.util.Optionals;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class AnnotationProperty implements Property {

    private final Elements elements;

    private final Types types;

    private final Lazy<Element> element;

    private final Optional<VariableElement> field;

    private final Optional<PropertyDescriptor> descriptor;

    private final Optional<ExecutableElement> readMethod;

    private final Optional<ExecutableElement> writeMethod;

    private final Lazy<String> name;

    private final Lazy<TypeMirror> type;

    private final Lazy<Boolean> isIdProperty;

    private final Lazy<Boolean> isReference;

    private final Lazy<Boolean> isVersion;

    private final Lazy<Boolean> isTransient;

    private final Lazy<Boolean> isCollection;

    private final Lazy<Boolean> isMap;

    private final TypeElement javaTypeElement;

    private List<String> referenceProperties;

    private ReferenceMode referenceMode;

    private Map<String, String> referenceColumns;

    public AnnotationProperty(final ProcessingEnvironment processEnv, final Optional<VariableElement> field,
        final Optional<PropertyDescriptor> descriptor) {

        this.elements = processEnv.getElementUtils();
        this.types = processEnv.getTypeUtils();
        this.field = field;
        this.descriptor = descriptor;

        if (descriptor.isPresent()) {
            this.readMethod = descriptor.get().getGetter();
            this.writeMethod = descriptor.get().getSetter();
        } else {
            this.readMethod = Optional.empty();
            this.writeMethod = Optional.empty();
        }

        this.element = Lazy.of(() -> withFieldOrMethod(Function.identity(), Function.identity(), Function.identity()));
        this.name = Lazy
            .of(() -> withFieldOrDescriptor(it -> it.getSimpleName().toString(), PropertyDescriptor::getName));

        this.type = Lazy.of(() ->
            withFieldOrMethod(
                Element::asType,
                setter -> setter.getParameters().get(0).asType(),
                ExecutableElement::getReturnType
            )
        );

        this.isCollection = Lazy.of(() -> initIsCollection(getType()));
        this.isMap = Lazy.of(() -> initIsMap(getType()));

        this.isTransient = Lazy.of(hasAnnotation(Transient.class));
        this.isIdProperty = Lazy.of(!isTransient() && hasAnnotation(PrimaryKey.class));
        this.isReference = Lazy.of(!isTransient() && hasAnnotation(Reference.class));
        this.isVersion = Lazy.of(!isTransient() && isAnnotationPresent(Version.class));

        PropertyJavaTypeVisitor propertyJavaTypeVisitor = new PropertyJavaTypeVisitor();
        this.javaTypeElement = getType().accept(propertyJavaTypeVisitor, this);

        if (isReference()) {
            initReferenceColumn(getAnnotation(Reference.class));
        }

    }

    private <T> T withFieldOrMethod(final Function<? super VariableElement, T> field,
        final Function<? super ExecutableElement, T> setter,
        final Function<? super ExecutableElement, T> getter
    ) {

        return Optionals.firstNonEmpty(
            () -> this.field.map(field),
            () -> this.writeMethod.map(setter),
            () -> this.readMethod.map(getter))
            .orElseThrow(
                () -> new IllegalStateException("Should not occur! Either field or descriptor has to be given"));
    }

    private <T> T withFieldOrDescriptor(final Function<? super VariableElement, T> field,
        final Function<? super PropertyDescriptor, T> descriptor) {

        return Optionals.firstNonEmpty(
            () -> this.field.map(field),
            () -> this.descriptor.map(descriptor))
            .orElseThrow(
                () -> new IllegalStateException("Should not occur! Either field or descriptor has to be given"));
    }

    private boolean initIsCollection(final TypeMirror type) {
        return types.isAssignable(types.erasure(type), getTypeElement(Collection.class).asType());
    }

    private TypeElement getTypeElement(final Class<?> type) {

        return elements.getTypeElement(type.getCanonicalName());
    }

    private void initReferenceColumn(final Reference ann) {

        initReference(ann.mode(), ann.properties(), ann.delimiter());
    }

    private boolean initIsMap(final TypeMirror type) {

        return types.isAssignable(types.erasure(type), getTypeElement(Map.class).asType());
    }

    @Override
    @org.springframework.lang.NonNull
    public Element getElement() {
        return this.element.get();
    }

    private void initReference(final ReferenceMode mode, final String[] properties, final String delimiter) {

        this.referenceMode = mode;
        List<String> referencePropertiesLocal = new ArrayList<>(properties.length);
        Map<String, String> referenceColumnsLocal = new HashMap<>(properties.length);
        for (String property : properties) {
            if (property.contains(delimiter)) {
                final String[] split = property.split(delimiter);
                referencePropertiesLocal.add(split[0]);
                referenceColumnsLocal.put(split[0], split[1]);
            } else {
                referencePropertiesLocal.add(property);
            }
        }
        this.referenceProperties = referencePropertiesLocal;
        this.referenceColumns = referenceColumnsLocal;
    }

    @Override
    public TypeElement getJavaTypeElement() {
        return javaTypeElement;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public TypeMirror getType() {
        return type.get();
    }

    @Override
    public boolean isIdProperty() {
        return isIdProperty.get();
    }

    @Override
    public boolean isVersion() {
        return isVersion.get();
    }

    @Override
    public boolean isCollection() {
        return isCollection.get();
    }

    @Override
    public boolean isMap() {
        return isMap.get();
    }

    @Override
    public boolean isReference() {
        return isReference.get();
    }

    @Override
    public ReferenceMode referenceMode() {
        return referenceMode;
    }

    @Override
    public List<String> referenceProperties() {
        return referenceProperties;
    }

    @Override
    public String referenceColumn(final String property) {

        return referenceColumns == null ? null : referenceColumns.get(property);
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {

        return getElement().getAnnotation(annotationType);
    }

    @Override
    public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {

        return Annotations.isAnnotationPresent(getElement(), annotationType);
    }

    @Override
    public boolean isTransient() {

        return isTransient != null && Boolean.TRUE.equals(isTransient.get());
    }

    private TypeElement getPrimitiveTypeElement(final TypeKind kind) {

        switch (kind) {
            case BOOLEAN:
                return getTypeElement(Boolean.class);
            case BYTE:
                return getTypeElement(Byte.class);
            case SHORT:
                return getTypeElement(Short.class);
            case INT:
                return getTypeElement(Integer.class);
            case LONG:
                return getTypeElement(Long.class);
            case CHAR:
                return getTypeElement(Character.class);
            case FLOAT:
                return getTypeElement(Float.class);
            case DOUBLE:
                return getTypeElement(Double.class);
            default:
                throw new IllegalArgumentException("不支持的类型：" + kind);

        }

    }

    private static final class PropertyJavaTypeVisitor extends SimpleTypeVisitor8<TypeElement, AnnotationProperty> {

        /**
         * Represents a primitive type.  These include {@code boolean}, {@code byte}, {@code short}, {@code int}, {@code long}, {@code char}, {@code float}, and
         * {@code double}.
         */
        @Override
        public TypeElement visitPrimitive(final PrimitiveType type, final AnnotationProperty property) {

            return property.getPrimitiveTypeElement(type.getKind());
        }

        @Override
        public TypeElement visitArray(final ArrayType array, final AnnotationProperty property) {

            TypeMirror type = array.getComponentType();
            TypeKind kind = type.getKind();
            if (kind.isPrimitive()) {
                return property.getPrimitiveTypeElement(kind);
            }

            if (type instanceof ArrayType || property.initIsCollection(type)) {
                throw new IllegalArgumentException("不支持的Array类型：[]" + type.toString());
            }

            if (type instanceof DeclaredType) {
                DeclaredType declaredType = (DeclaredType) type;
                Element element = declaredType.asElement();
                if (element instanceof TypeElement) {
                    return (TypeElement) element;
                }
            }

            throw new IllegalArgumentException("不支持的Array类型：[]" + type.toString());
        }

        @Override
        public TypeElement visitDeclared(final DeclaredType type, final AnnotationProperty property) {

            if (property.isMap()) {
                return property.getTypeElement(Map.class);
            }

            if (property.isCollection()) {
                TypeMirror elementType = type.getTypeArguments().get(0);
                TypeKind kind = elementType.getKind();
                if (kind.isPrimitive()) {
                    return property.getPrimitiveTypeElement(kind);
                }
                if (TypeKind.DECLARED == kind && !property.initIsCollection(elementType)) {
                    DeclaredType declaredType = (DeclaredType) elementType;
                    Element element = declaredType.asElement();
                    if (element instanceof TypeElement) {
                        return (TypeElement) element;
                    }
                }

            }

            Element element = type.asElement();
            if (element instanceof TypeElement) {
                return (TypeElement) element;
            }

            throw new IllegalArgumentException("不支持的Array类型：[]" + type.toString());
        }

    }

}
