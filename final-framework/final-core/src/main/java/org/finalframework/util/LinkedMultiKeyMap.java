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

package org.finalframework.util;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2019-12-17 12:53:41
 * @since 1.0
 */
public class LinkedMultiKeyMap<K1, K2, V> extends LinkedHashMap<K1, Map<K2, V>> implements MultiKeyMap<K1, K2, V> {

    @Override
    public void add(K1 key1, K2 key2, V value) {
        Map<K2, V> map = computeIfAbsent(key1, k -> new LinkedHashMap<>());
        map.put(key2, value);
    }

    @Override
    public V getOrDefault(K1 key1, K2 key2, V defValue) {
        Map<K2, V> map = get(key1);
        return map == null ? defValue : map.getOrDefault(key2, defValue);
    }
}

