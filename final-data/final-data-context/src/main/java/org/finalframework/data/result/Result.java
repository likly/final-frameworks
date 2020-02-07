package org.finalframework.data.result;

import org.finalframework.data.entity.PageInfo;
import org.finalframework.data.exception.IException;
import org.finalframework.data.response.ResponseStatus;
import org.finalframework.data.response.Responsible;

import java.io.Serializable;

/**
 * 业务数据返回结果封装，统一业务返回的数据结构。
 *
 * @author likly
 * @version 1.0
 * @date 2018-09-26 21:08
 * @see Page
 * @since 1.0
 */
public final class Result<T> implements Responsible, Serializable {

    private static final long serialVersionUID = -2801752781032532754L;
    /**
     * 状态码，描述一次请求的状态
     *
     * @see ResponseStatus#SUCCESS
     * @see ResponseStatus#BAD_REQUEST
     * @see ResponseStatus#FORBIDDEN
     * @see ResponseStatus#NOT_FOUND
     * @see ResponseStatus#INTERNAL_SERVER_ERROR
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String description;

    /**
     * 业务状态码
     */
    private String code;
    /**
     * 业务状态描述
     */
    private String message;
    /**
     * 业务数据
     */
    private T data;
    /**
     * 分页信息
     */
    private PageInfo page;
    /**
     * trace
     */
    private String trace;
    /**
     * 响应时间戳
     */
    private Long timestamp = System.currentTimeMillis();
    /**
     * 执行时长
     */
    private Long duration;

    private Class<?> view;

    private Class<Throwable> exception;

    public Result() {
    }

    public Result(Integer status, String description, String code, String message, T data) {
        this.status = status;
        this.description = description;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(Integer status, String description, String code, String message) {
        this.status = status;
        this.description = description;
        this.code = code;
        this.message = message;
    }

    public Result(Integer status, String description, T data) {
        this(status, description, status.toString(), description, data);
    }

    public Result(Integer status, String description) {
        this(status, description, status.toString(), description);
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Class<?> getView() {
        return view;
    }

    public void setView(Class<?> view) {
        this.view = view;
    }

    public Class<Throwable> getException() {
        return exception;
    }

    public void setException(Class<Throwable> exception) {
        this.exception = exception;
    }

    public boolean isSuccess() {
        return ResponseStatus.SUCCESS.getCode().equals(status);
    }

}
