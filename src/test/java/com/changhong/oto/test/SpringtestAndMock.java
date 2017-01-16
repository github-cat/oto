package com.changhong.oto.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = { "classpath:spring/access-control.xml", "classpath:spring/dao.xml",
        "classpath:spring/property.xml", "classpath:spring/service.xml" })
public class SpringtestAndMock {
	
}
