/*
 * @(#)HttpPerformer.java	1.0 2017年5月16日 下午1:54:21
 *
 * Copyright 2004-2010 Client Server International, Inc. All rights reserved.
 * CSII PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zzsoft.commonhttp.httpclient;

import java.io.IOException;
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
public interface HttpPerformer {

    Object submit(Map<String, Object> data) throws IOException;
}
