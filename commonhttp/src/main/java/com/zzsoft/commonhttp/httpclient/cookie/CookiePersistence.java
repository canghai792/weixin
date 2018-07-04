package com.zzsoft.commonhttp.httpclient.cookie;

import java.util.List;
import java.util.Map;

/**
 * <p>Title:cookie持久化处理</p>
 * <p>Description:
 * </p>
 *
 * @author cyj
 * @version 1.0
 * @createDate：2017-5-16
 */
public interface CookiePersistence {

    /**
     * 查询session
     *
     * @param context
     * @return List<CookieBean>
     * @throws PeException
     */
    public List<CookieBean> querySession(Map<String, Object> params) throws Exception;


    /**
     * 保存session
     *
     * @param cookieBean
     * @param params
     * @throws PeException
     */
    public void savaSession(List<CookieBean> cookieBeanList, Map<String, Object> params) throws Exception;

}
