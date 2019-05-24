package org.finalframework.data.query;

import org.springframework.lang.Nullable;

/**
 * 可分页的
 *
 * @author likly
 * @version 1.0
 * @date 2019-02-18 19:21:21
 * @since 1.0
 */
public interface Pageable {
    /**
     * 返回分页页码
     *
     * @return 分页页码
     */
    @Nullable
    Integer getPage();

    /**
     * 返回页面容量
     *
     * @return 页面容量
     */
    @Nullable
    Integer getSize();

    /**
     * 是否进行Count统计
     *
     * @return Count统计
     */
    @Nullable
    Boolean getCount();

    /**
     * 分页合理化
     *
     * @return 分页合理化
     */
    @Nullable
    Boolean getReasonable();

    /**
     * true且pageSize=0时返回全部结果，false时分页,null时用默认配置
     *
     * @return true且pageSize=0时返回全部结果，false时分页,null时用默认配置
     */
    @Nullable
    Boolean getPageSizeZero();
}
