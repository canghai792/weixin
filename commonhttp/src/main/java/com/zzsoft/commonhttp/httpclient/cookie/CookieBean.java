package com.zzsoft.commonhttp.httpclient.cookie;

import java.io.Serializable;

/**
 * <p>Title:CookieBean</p>
 * <p>Description:
 * </p>
 *
 * @author cyj
 * @version 1.0
 * @createDate：2015-5-16
 */
public class CookieBean implements Serializable {

    /*** */
    private static final long serialVersionUID = -3507204772124618520L;

    private String name;

    private String value;

    private String domain;

    private String path;

    public CookieBean() {
        super();
    }

    public CookieBean(String name, String value, String domain, String path) {
        super();
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {

        return new StringBuilder().append("domain:").append(domain).
                append(" path:").append(path).append(" name:").append(name).append(" value").append(value).toString();

    }
}
