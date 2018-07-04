/*
 * @(#)DefaultCookieSavingPolicy.java	1.0 2017年5月16日 下午5:34:19
 *
 * Copyright 2004-2010 Client Server International, Inc. All rights reserved.
 * CSII PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zzsoft.commonhttp.httpclient.cookie;

import com.zzsoft.commonhttp.httpclient.CookieSavingPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.ArrayList;
import java.util.List;
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
public class DefaultCookieSavingPolicy implements CookieSavingPolicy {

    protected Log logger = LogFactory.getLog(getClass());

    private CookiePersistence persistence;

    /* (non-Javadoc)
     * @see CookieSavingPolicy#send(org.apache.http.client.CookieStore, java.util.Map)
     */
    @Override
    public void send(CookieStore cookieStore, Map<String, Object> params) throws Exception {
        
        List<CookieBean> cookieList = persistence.querySession(params);

        if (cookieList != null && cookieList.size() > 0) {
            // 如果存在交易会话数据，使用交易会话数据请求交易服务器，否则不上送cookie，等待交易服务器下发cookie
            for (int rc = 0; rc < cookieList.size(); rc++) {
                CookieBean reqC = (CookieBean) cookieList.get(rc);
                BasicClientCookie cookie = new BasicClientCookie(reqC.getName(), reqC.getValue());
                cookie.setDomain(reqC.getDomain());
                cookie.setPath(reqC.getPath());
                cookieStore.addCookie(cookie);
                if (logger.isInfoEnabled()) {
                    logger.info("domain = " + reqC.getDomain() + ": path = "
                            + reqC.getPath() + ": name = " + reqC.getName()
                            + ": value = " + reqC.getValue());
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see CookieSavingPolicy#save(org.apache.http.client.CookieStore, java.util.Map)
     */
    @Override
    public void save(CookieStore cookieStore, Map<String, Object> params) throws Exception {
        

        List<Cookie> cookies = cookieStore.getCookies();

        if (cookies != null && cookies.size() > 0) {
            List<CookieBean> resCookieList = new ArrayList<CookieBean>();
            for (int i = 0; i < cookies.size(); i++) {
                String name = cookies.get(i).getName();
                String value = cookies.get(i).getValue();
                CookieBean cookieBean = new CookieBean(name, value, cookies.get(i).getDomain(), cookies.get(i).getPath());
                resCookieList.add(cookieBean);
                if (logger.isInfoEnabled()) {
                    logger.info("domain = " + cookies.get(i).getDomain()
                            + ": path = " + cookies.get(i).getPath() + ": name = "
                            + cookies.get(i).getName() + ": value = "
                            + cookies.get(i).getValue());
                }
            }
            if (resCookieList.size() > 0) {
                persistence.savaSession(resCookieList, params);
            }
        }
    }

}
