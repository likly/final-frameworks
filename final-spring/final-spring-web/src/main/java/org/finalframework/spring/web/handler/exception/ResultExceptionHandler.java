package org.finalframework.spring.web.handler.exception;

import org.finalframework.data.result.Result;

/**
 * {@link Result}异常处理器，将异常所携带的异常消息封装成 {@link Result}对象返回给调用方。
 *
 * @author likly
 * @version 1.0
 * @date 2018-10-31 13:14
 * @since 1.0
 */
public interface ResultExceptionHandler extends RestExceptionHandler<Result> {

}
