package cn.polarismesh.agent.core.spring.cloud.registry;

import cn.polarismesh.agent.core.spring.cloud.context.PolarisAgentProperties;
import cn.polarismesh.agent.core.spring.cloud.context.PolarisContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Polaris Spring Cloud Registration 实现
 */
public class PolarisRegistration implements Registration, ServiceInstance {

    private final PolarisAgentProperties polarisProperties;

    public PolarisRegistration(PolarisContext polarisContext) {
        this.polarisProperties = polarisContext.getPolarisContextAgentProperties();
    }

    @Override
    public String getServiceId() {
        return polarisProperties.getService();
    }

    @Override
    public String getHost() {
        return polarisProperties.getHost();
    }

    @Override
    public int getPort() {
        return polarisProperties.getPort();
    }

    public void setPort(int port) {
        this.polarisProperties.setPort(port);
    }

    @Override
    public boolean isSecure() {
        return StringUtils.equalsIgnoreCase(polarisProperties.getProtocol(), "https") ||
                StringUtils.equalsIgnoreCase(polarisProperties.getProtocol(), "grpc");
    }

    @Override
    public URI getUri() {
        return DefaultServiceInstance.getUri(this);
    }

    @Override
    public Map<String, String> getMetadata() {
        return new HashMap<>();
    }

    public PolarisAgentProperties getPolarisProperties() {
        return polarisProperties;
    }

    @Override
    public String toString() {
        return "PolarisRegistration{" +
                ", polarisProperties=" + polarisProperties +
                '}';
    }
}
