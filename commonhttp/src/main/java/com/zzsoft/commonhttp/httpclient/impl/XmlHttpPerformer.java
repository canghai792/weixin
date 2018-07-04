package com.zzsoft.commonhttp.httpclient.impl;

import java.io.IOException;
import java.util.Map;

/**
 * 尚未实现，不可使用
 * xml报文httpperformer
 *
 * @author cyj
 * <p>
 * Created on 2017年5月16日
 * </p>
 * @version 1.0
 * @since 1.0
 */
public class XmlHttpPerformer extends AbstractHttpPerformer {

    /* (non-Javadoc)
     * @see AbstractHttpPerformer#formatRequest(java.util.Map)
     */
    @Override
    public byte[] formatRequest(Map<String, Object> data) throws IOException {
        
        throw new UnsupportedOperationException("Need implment.");
    }

    /* (non-Javadoc)
     * @see AbstractHttpPerformer#parseResponse(byte[])
     */
    @Override
    public Object parseResponse(byte[] byteArray) throws IOException {
        
        throw new UnsupportedOperationException("Need implment.");
    }
}
