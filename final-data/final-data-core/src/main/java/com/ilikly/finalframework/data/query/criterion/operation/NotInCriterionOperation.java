package com.ilikly.finalframework.data.query.criterion.operation;

import com.ilikly.finalframework.data.query.CriterionOperator;
import com.ilikly.finalframework.data.query.DefaultCriterionOperator;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-18 14:25:39
 * @since 1.0
 */
public abstract class NotInCriterionOperation<E> extends AbsCollectionCriterionOperation<E> {

    @Override
    public final CriterionOperator operator() {
        return DefaultCriterionOperator.NOT_IN;
    }

}
