package org.finalframework.data.repository.trigger;

import java.io.Serializable;
import java.util.Collection;
import org.finalframework.data.annotation.IEntity;

/**
 * @author sli
 * @version 1.0
 * @date 2020-04-02 22:07:44
 * @see org.finalframework.data.repository.Repository#insert(String, Class, boolean, Collection)
 * @see org.finalframework.data.repository.Repository#replace(String, Class, Collection)
 * @see org.finalframework.data.repository.Repository#save(String, Class, Collection)
 * @since 1.0
 */
public interface InsertTrigger<ID extends Serializable, T extends IEntity<ID>> extends Trigger {

    void beforeInsert(String tableName, Class<?> view, boolean ignore, Collection<T> entities);

    void afterInsert(String tableName, Class<?> view, boolean ignore, Collection<T> entities, int rows);

}
