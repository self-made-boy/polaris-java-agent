package cn.polarismesh.plugin.dubbo2.interceptor;

public interface Interceptor {
    void before(Object target, Object[] args);

    void after(Object target, Object[] args, Object result, Throwable throwable);
}
