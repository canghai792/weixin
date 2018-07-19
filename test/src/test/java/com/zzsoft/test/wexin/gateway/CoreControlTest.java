package com.zzsoft.test.wexin.gateway;

import com.zzsoft.commonhttp.httpclient.HttpPerformer;
import com.zzsoft.commonhttp.httpclient.util.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Test
@ContextConfiguration(locations = { "classpath:spring-test-config.xml" })
public class CoreControlTest extends AbstractTestNGSpringContextTests {

    @Qualifier("wxprocessdefaulthttp")
    @Autowired
    private HttpPerformer httpPerformer;

    @Test
    public void printOK(){
        Map map = new HashMap();
        map.put(Dictionary.REQUEST_URL, "/getUserInfo");
        map.put(Dictionary.REQUEST_METHOD, "get");
        Map dataMap = new HashMap();

        dataMap.put("openid","oXiH5shjAgVPUFfScR6QZyW587mQ");
        dataMap.put("lang","zh_CN");
        map.put(Dictionary.REQUEST_DATA, dataMap);
        try {
           Object result = httpPerformer.submit(map);
           System.out.println(new String((byte[]) result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
