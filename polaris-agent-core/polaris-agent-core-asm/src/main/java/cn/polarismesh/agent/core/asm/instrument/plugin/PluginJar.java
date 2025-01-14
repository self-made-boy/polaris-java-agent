/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.polarismesh.agent.core.asm.instrument.plugin;

import cn.polarismesh.agent.core.common.utils.FileUtils;
import cn.polarismesh.agent.core.common.utils.JarFileUtils;
import cn.polarismesh.agent.core.common.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;

public class PluginJar {

    public static final String PLUGIN_ID = "Plugin-Id";
    public static final String PLUGIN_PACKAGE = "Plugin-Package";
    public static final String PLUGIN_COMPILER_VERSION = "Plugin-Compiler-Version";
    public static final String DEFAULT_PLUGIN_PACKAGE_NAME = "cn.polarismesh.agent.core.asm.extension";

    private final URL url;
    private final JarFile jarFile;

    private final String pluginId;
    private final String pluginCompilerVersion;
    private final List<String> pluginPackages;

    public PluginJar(URL url, JarFile jarFile) {
        this.url = Objects.requireNonNull(url, "url");
        this.jarFile = Objects.requireNonNull(jarFile, "jarFile");
        this.pluginId = JarFileUtils.getManifestValue(jarFile, PLUGIN_ID, null);
        this.pluginCompilerVersion = JarFileUtils.getManifestValue(jarFile, PLUGIN_COMPILER_VERSION, null);
        String pluginPackages = JarFileUtils
                .getManifestValue(jarFile, PLUGIN_PACKAGE, DEFAULT_PLUGIN_PACKAGE_NAME);
        this.pluginPackages = StringUtils.tokenizeToStringList(pluginPackages, ",");
    }

    public static PluginJar fromFilePath(String filePath) {
        final File file = toFile(filePath);
        return fromFile(file);
    }

    public static PluginJar fromFile(File file) {
        final URL url = toURL(file);
        final JarFile jarFile = createJarFile(file);
        return new PluginJar(url, jarFile);
    }

    private static File toFile(String filePath) {
        final File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException(file + " File does not exist");
        }
        if (!file.isFile()) {
            throw new RuntimeException(file + " is not a file");
        }
        if (!file.canRead()) {
            throw new RuntimeException(file + " File cannot be read");
        }
        return file;
    }

    private static URL toURL(File file) {
        try {
            return FileUtils.toURL(file);
        } catch (IOException e) {
            throw new RuntimeException("Invalid URL:" + file);
        }
    }

    private static JarFile createJarFile(File pluginJar) {
        try {
            return new JarFile(pluginJar);
        } catch (IOException e) {
            throw new RuntimeException("IO error. " + e.getCause(), e);
        }
    }

    public URL getUrl() {
        return url;
    }

    public JarFile getJarFile() {
        return jarFile;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getPluginCompilerVersion() {
        return pluginCompilerVersion;
    }

    public List<String> getPluginPackages() {
        return pluginPackages;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PluginJar{");
        sb.append("url=").append(url);
        sb.append(", jarFile=").append(jarFile);
        sb.append(", pluginId='").append(pluginId).append('\'');
        sb.append(", pluginCompilerVersion='").append(pluginCompilerVersion).append('\'');
        sb.append(", pluginPackages=").append(pluginPackages);
        sb.append('}');
        return sb.toString();
    }
}
