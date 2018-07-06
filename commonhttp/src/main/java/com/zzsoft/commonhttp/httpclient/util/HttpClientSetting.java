package com.zzsoft.commonhttp.httpclient.util;

/**
 *
 */
public class HttpClientSetting {

    /**
     * 建立连接超时时间
     */
    private int connectTimeout;

    /**
     * 从连接池获取连接超时时间
     */
    private int connectionRequestTimeout;

    /**
     * 读取超时时间
     */
    private int socketTimeout;

    /**
     * 等待扫描空闲连接时间
     */
    private int sleepTime;

    /**
     * 最大空闲超时时间
     */
    private int maxIdleTime;

    /**
     * 默认最大连接数
     */
    private int maxTotal;

    /**
     * 默认每个路由最大连接数
     */
    private int maxPerRoute;

    /**
     * 主机地址
     */
    private String url;

    /**
     * 代理代理主机IP
     */
    private String proxyHost;

    /**
     * 代理代理主机端口
     */
    private int proxyPort;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
