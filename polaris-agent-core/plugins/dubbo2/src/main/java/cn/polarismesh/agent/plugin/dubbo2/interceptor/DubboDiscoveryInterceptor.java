/*
 * Tencent is pleased to support the open source community by making Polaris available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package cn.polarismesh.agent.plugin.dubbo2.interceptor;

import cn.polarismesh.agent.plugin.dubbo2.entity.InstanceInvoker;
import cn.polarismesh.common.interceptor.AbstractInterceptor;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invoker;

import java.util.Map;

/**
 * interceptor for com.alibaba.dubbo.registry.integration.RegistryDirectory#toInvokers(java.util.List)
 */
public class DubboDiscoveryInterceptor implements AbstractInterceptor {

    @Override
    public void before(Object target, Object[] args) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        Map<URL, Invoker<?>> invokers = (Map<URL, Invoker<?>>) result;
        if (null == invokers || invokers.size() == 0) {
            return;
        }
        invokers.replaceAll((k, v) -> new InstanceInvoker<>(v));
    }
}
