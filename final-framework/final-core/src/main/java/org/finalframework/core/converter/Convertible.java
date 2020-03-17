package org.finalframework.core.converter;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-22 20:54:20
 * @since 1.0
 */
@FunctionalInterface
public interface Convertible<TARGET> {
    TARGET convert();
}