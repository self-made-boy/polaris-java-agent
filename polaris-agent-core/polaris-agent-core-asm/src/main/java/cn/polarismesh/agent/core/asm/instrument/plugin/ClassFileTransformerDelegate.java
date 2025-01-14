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

package cn.polarismesh.agent.core.asm.instrument.plugin;

import cn.polarismesh.agent.core.common.exception.InstrumentException;
import cn.polarismesh.agent.core.common.exception.PolarisAgentException;
import cn.polarismesh.agent.core.asm.instrument.GuardInstrumentor;
import cn.polarismesh.agent.core.asm.instrument.InstrumentContext;
import cn.polarismesh.agent.core.extension.transform.TransformCallback;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Objects;

public class ClassFileTransformerDelegate implements ClassFileTransformer {

    private final InstrumentContext instrumentContext;
    private final TransformCallbackProvider transformCallbackProvider;

    public ClassFileTransformerDelegate(InstrumentContext instrumentContext,
            TransformCallbackProvider transformCallbackProvider) {
        this.instrumentContext = Objects.requireNonNull(instrumentContext, "instrumentContext");
        this.transformCallbackProvider = Objects.requireNonNull(transformCallbackProvider, "transformCallbackProvider");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        Objects.requireNonNull(className, "className");

        final InstrumentContext instrumentContext = this.instrumentContext;
        final GuardInstrumentor guard = new GuardInstrumentor(instrumentContext);
        try {
            // WARN external plugin api
            final TransformCallback transformCallback = this.transformCallbackProvider
                    .getTransformCallback(instrumentContext, loader);
            return transformCallback
                    .doInTransform(guard, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        } catch (InstrumentException e) {
            throw new PolarisAgentException(e);
        } finally {
            guard.close();
        }
    }

    @Override
    public String toString() {
        return "ClassFileTransformerDelegate{" +
                "transformCallbackProvider=" + transformCallbackProvider +
                '}';
    }
}
