package com.zzsoft.commonhttp.httpclient.impl;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * json报文httpperformer
 *
 * @author cyj
 * <p>
 * Created on 2017年5月16日
 * </p>
 * @version 1.0
 * @since 1.0
 */
public class JsonHttpPerformer extends AbstractHttpPerformer {

    /* (non-Javadoc)
     * @see com.csii.ibs.common.httpclient.AbstractHttpPerformer#formatRequest(java.util.Map)
     */
    @Override
    public byte[] formatRequest(Map<String, Object> data) throws IOException {
        
        String sendData = JSON.toJSONString(data);
        return sendData.getBytes(super.getEncoding());
    }

    /* (non-Javadoc)
     * @see com.csii.ibs.common.httpclient.AbstractHttpPerformer#parseResponse(byte[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object parseResponse(byte[] byteArray) throws IOException {
        
        try {
            String recvData = new String(byteArray, super.getEncoding());
            Map<String, Object> jsonMap = JSON.parseObject(recvData, HashMap.class);
            return jsonMap;
        } catch (UnsupportedEncodingException e) {
            
            super.logger.error(e, e);
            throw new IOException("Response Encoding Error!");
        }
    }

}
