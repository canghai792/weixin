package com.zzsoft.commonhttp.httpclient;

import org.apache.http.client.CookieStore;

import java.util.Map;

/**
 * {type specification, must edit}
 *
 * @author lenovo {must edit, use true name}
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
public interface CookieSavingPolicy {

    void send(CookieStore cookieStore, Map<String, Object> params) throws Exception;

    void save(CookieStore cookieStore, Map<String, Object> params) throws Exception;
}
