package cn.com.likly.finalframework.spring.web.exception;

import cn.com.likly.finalframework.data.result.Result;

/**
 * @author likly
 * @version 1.0
 * @date 2018-10-31 13:14
 * @since 1.0
 */
public interface ResultExceptionHandler<E extends Throwable> extends ExceptionHandler<E, Result> {

}
