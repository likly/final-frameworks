/*
 * Copyright (c) 2018-2020.  the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.finalframework.data.result;

import org.finalframework.data.entity.PageImpl;

import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2018-09-26 21:09
 * @since 1.0
 */
public class Page<E> extends PageImpl<List<E>> {

    public Page() {
    }

    private Page(Builder<E> builder) {
        setPage(builder.page);
        setSize(builder.size);
        setPages(builder.pages);
        setTotal(builder.total);
        setFirstPage(builder.firstPage);
        setLastPage(builder.lastPage);
        setResult(builder.result);
    }


    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E> implements org.finalframework.core.Builder<Page<E>> {
        private Integer page;
        private Integer size;
        private Integer pages;
        private Long total;
        private List<E> result;
        private Boolean firstPage;
        private Boolean lastPage;

        public Builder<E> page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder<E> size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder<E> pages(Integer pages) {
            this.pages = pages;
            return this;
        }

        public Builder<E> total(Long total) {
            this.total = total;
            return this;
        }


        public Builder<E> result(List<E> result) {
            this.result = result;
            return this;
        }

        public Builder<E> firstPage(boolean firstPage) {
            this.firstPage = firstPage;
            return this;
        }

        public Builder<E> lastPage(boolean lastPage) {
            this.lastPage = lastPage;
            return this;
        }

        @Override
        public Page<E> build() {
            return new Page<E>(this);
        }
    }
}
