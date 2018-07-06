package com.zzsoft.commonhttp.httpclient.impl;

import com.zzsoft.commonhttp.httpclient.CookieSavingPolicy;
import com.zzsoft.commonhttp.httpclient.HttpPerformer;
import com.zzsoft.commonhttp.httpclient.util.Constants;
import com.zzsoft.commonhttp.httpclient.util.Dictionary;
import com.zzsoft.commonhttp.httpclient.util.HttpClientSetting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.IdleConnectionEvictor;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient 抽象服务类，
 * 提供get和post请求处理方法。
 * <p>
 * 根据通讯报文不同实现实体类
 *
 * @author cyj
 * <p>
 * Created on 2017年5月16日
 * Modification history
 * {add your history}
 * </p>
 * <p>
 * IBS Product Expert Group, CSII
 * Powered by CSII PowerEngine 6.0
 * </p>
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractHttpPerformer implements HttpPerformer {

    protected Log logger = LogFactory.getLog(AbstractHttpPerformer.class);

    private static final String HTTP = "http";

    private static final String HTTPS = "https";

    private SSLConnectionSocketFactory sslsf = null;

    private PoolingHttpClientConnectionManager cm = null;

    private SSLContextBuilder builder = null;

    private IdleConnectionEvictor scanThread = null;

    private CloseableHttpClient client = null;

    private CookieStore cookieStore = null;

    private HttpClientSetting setting = null;

    private String encoding = "UTF-8";

    private String defaultContentType;

    private CookieSavingPolicy sessionSavingPolicy;

    private void init() throws IOException {
        if (client == null) {
            try {
                builder = new SSLContextBuilder();
                // 全部信任 不做身份鉴定
                builder.loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                });
                sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"TLSv1", "TLSv1.2"},
                        null, NoopHostnameVerifier.INSTANCE);

                Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();

                cm = new PoolingHttpClientConnectionManager(registry);
                cm.setMaxTotal(setting.getMaxTotal());
                cm.setDefaultMaxPerRoute(setting.getMaxPerRoute());

                // 扫描无效连接的线程
                scanThread = new IdleConnectionEvictor(cm, setting.getSleepTime(), TimeUnit.SECONDS, setting.getMaxIdleTime(), TimeUnit.SECONDS);
                scanThread.start();

                RequestConfig.Builder builder = RequestConfig.custom().setConnectTimeout(setting.getConnectTimeout())
                        .setConnectionRequestTimeout(setting.getConnectionRequestTimeout()).setSocketTimeout(setting.getSocketTimeout());

                if (!(setting.getProxyHost() == null || "".equals(setting.getProxyHost().trim()))) {
                    HttpHost proxy = new HttpHost(setting.getProxyHost(), setting.getProxyPort(), "http");
                    builder.setProxy(proxy);
                }

                RequestConfig config = builder.build();
                cookieStore = new BasicCookieStore();

                client = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).setConnectionManager(cm).build();
            } catch (Exception e) {
                logger.error(e, e);
                throw new IOException("Init Httpclient Error!");
            }
        }
    }

    @Override
    public final Object submit(Map<String, Object> data) throws IOException {

        if (client == null) {
            synchronized (this) {
                init();
            }
        }

        String method = (String) data.get(Dictionary.REQUEST_METHOD);
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;
        Object recvData = null;

        try {
            if (method.toUpperCase().equals(Constants.REQUEST_METHOD_GET)) {
                request = createHttpGet(data);
            } else if (method.toUpperCase().equals(Constants.REQUEST_METHOD_POST)) {
                request = createHttpPost(data);
            } else {
                request = createHttpPost(data);
            }

            String contentType = (String) data.get(Dictionary.CONTENT_TYPE);
            if (contentType == null) {
                contentType = defaultContentType;
            }
            if (contentType != null) {
                request.addHeader(Dictionary.CONTENT_TYPE, contentType);
            }
            request.addHeader(Dictionary.CONTENT_ENCODING, encoding);

            if (sessionSavingPolicy != null) {
                sessionSavingPolicy.send(cookieStore, data);
            }

            response = client.execute(request);
            recvData = parseResponse(EntityUtils.toByteArray(response.getEntity()));

            if (sessionSavingPolicy != null) {
                sessionSavingPolicy.save(cookieStore, data);
            }

            cookieStore.clear();
        } catch (Exception e) {
            
            logger.error(e, e);
            throw new IOException("Send Request Error!");
        } finally {
            
            if (request != null) {
                request.releaseConnection();
            }
            if (response != null) {
                response.getEntity().getContent().close();
                response.close();
            }
        }

        return recvData;
    }

    @SuppressWarnings("unchecked")
    private HttpRequestBase createHttpGet(Map<String, Object> data) throws IOException {

        String url = this.setting.getUrl();
        String queryString = "";

        Map<String, Object> requestData = (Map<String, Object>) data.get(Dictionary.REQUEST_DATA);
        if (requestData != null && requestData.size() > 0) {
            if (url.contains("?")) {
                queryString = "&";
            } else {
                queryString = "?";
            }

            Set<String> keySet = requestData.keySet();
            for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                String key = it.next();
                Object value = requestData.get(key);
                queryString = queryString + key + "=" + value + "&";
                logger.debug("parameters: " + key + "=" + value);
            }

            queryString = queryString.substring(0, queryString.length() - 1);
        }

        url = url + queryString;

        return new HttpGet(url);
    }

    @SuppressWarnings("unchecked")
    private HttpRequestBase createHttpPost(Map<String, Object> data) throws IOException {

        String url = this.setting.getUrl();

        HttpPost request = new HttpPost(url);
        Map<String, Object> requestData = (Map<String, Object>) data.get(Dictionary.REQUEST_DATA);
        if (requestData != null && requestData.size() > 0) {
            byte[] sendBytes = formatRequest(requestData);
            HttpEntity entity = new ByteArrayEntity(sendBytes);
            request.setEntity(entity);
        }

        return request;
    }

    /**
     * @param data
     * @return
     * @throws IOException
     * @version 1.0
     * @since 1.0
     */
    public abstract byte[] formatRequest(Map<String, Object> data) throws IOException;

    /**
     * @param byteArray
     * @return
     * @throws IOException
     * @version 1.0
     * @since 1.0
     */
    public abstract Object parseResponse(byte[] byteArray) throws IOException;

    /**
     * @return the setting
     */
    public HttpClientSetting getSetting() {
        return setting;
    }

    /**
     * @param setting the setting to set
     */
    public void setSetting(HttpClientSetting setting) {
        this.setting = setting;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the defaultContentType
     */
    public String getDefaultContentType() {
        return defaultContentType;
    }

    /**
     * @param defaultContentType the defaultContentType to set
     */
    public void setDefaultContentType(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    /**
     * @return the sessionSavingPolicy
     */
    public CookieSavingPolicy getSessionSavingPolicy() {
        return sessionSavingPolicy;
    }

    /**
     * @param sessionSavingPolicy the sessionSavingPolicy to set
     */
    public void setSessionSavingPolicy(CookieSavingPolicy sessionSavingPolicy) {
        this.sessionSavingPolicy = sessionSavingPolicy;
    }
}
