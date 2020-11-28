package org.ifinal.finalframework.auto.model;

import org.springframework.lang.NonNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractAnnotationValueVisitor8;
import java.util.List;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public final class AnnotationValues {

    private static final BooleanAnnotationVisitor BOOLEAN_ANNOTATION_VISITOR = new BooleanAnnotationVisitor();

    private AnnotationValues() {
    }

    public static boolean getBoolean(@NonNull AnnotationValue value) {
        return value.accept(BOOLEAN_ANNOTATION_VISITOR, null);
    }

    public static TypeElement getClass(@NonNull AnnotationValue value) {
        return (TypeElement) ((DeclaredType) value.getValue()).asElement();
    }

    private static class BooleanAnnotationVisitor extends AbstractAnnotationValueVisitor8<Boolean, Void> {

        @Override
        public Boolean visitBoolean(boolean av, Void unused) {
            return av;
        }

        @Override
        public Boolean visitByte(byte b, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitChar(char c, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitDouble(double d, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitFloat(float f, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitInt(int i, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitLong(long i, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitShort(short s, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitString(String s, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitType(TypeMirror t, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitEnumConstant(VariableElement c, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitAnnotation(AnnotationMirror a, Void unused) {
            throw new IllegalArgumentException("");
        }

        @Override
        public Boolean visitArray(List<? extends AnnotationValue> vals, Void unused) {
            throw new IllegalArgumentException("");
        }
    }

}