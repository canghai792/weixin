package com.zzsoft.commonhttp.httpclient.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 默认{key=value}方式httpperformer
 *
 * @author cyj
 * <p>
 * Created on 2017年5月16日
 * </p>
 * @version 1.0
 * @since 1.0
 */
public class DefaultHttpPerformer extends AbstractHttpPerformer {

    private String separator = "&";

    /* (non-Javadoc)
     * @see AbstractHttpPerformer#formatRequest(java.util.Map)
     */
    @Override
    public byte[] formatRequest(Map<String, Object> data) throws IOException {
        

        String sendData = "";

        for (Entry<String, Object> entry : data.entrySet()) {
            sendData = sendData + entry.getKey() + "=" + entry.getValue() + separator;
        }
        return sendData.substring(0, sendData.length()).getBytes(super.getEncoding());
    }

    /* (non-Javadoc)
     * @see AbstractHttpPerformer#parseResponse(byte[])
     */
    @Override
    public Object parseResponse(byte[] byteArray) throws IOException {
        
        return byteArray;
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

}
