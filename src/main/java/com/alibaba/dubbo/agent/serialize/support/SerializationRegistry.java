/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.agent.serialize.support;

import com.alibaba.dubbo.agent.serialize.Serialization;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class SerializationRegistry {

    private static final Map<String, Serialization> registrationsForName = new HashMap<>();
    private static final Map<Byte, Serialization> registrationsForId = new HashMap<>();


    public static void registerSerialization(Serialization serialization) {
        registrationsForName.put(serialization.getName(), serialization);
        log.info("registered Serialization:" + serialization.getName() + "->" + serialization.getClass().getName());
        registrationsForId.put(serialization.getContentTypeId(), serialization);
        log.info("registered Serialization:" + serialization.getContentTypeId() + "->" + serialization.getClass().getName());
    }

    public static Serialization getSerialization(String name) {
        return registrationsForName.get(name);
    }

    public static Serialization getSerialization(int id) {
        return registrationsForId.get(id);
    }
}
