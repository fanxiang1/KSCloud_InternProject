package com.ksyun.trade;

import com.ksyun.trade.utils.RedisUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 基础测试类.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class BaseSpringAllTest {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtils redisUtils;

    long s = 0;
    long e = 0;

    @Before
    public void before() {
        s = System.currentTimeMillis();
    }

    @After
    public void after() {
        e = System.currentTimeMillis();
        logger.info("耗时:{}秒", (e - s) / 1000);
    }

    @Test
    public void ping() {
        logger.info("ok");
    }

    // 测试redis工具类是否可用
    @Test
    public void testRedis(){
        Boolean fanxiang = redisUtils.hasKey("fanxiang");
        System.out.println(fanxiang);
    }
}
