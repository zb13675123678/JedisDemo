package com.qfedu;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @auther Zhangbo
 * @date 2020/4/28 18:39
 */
public class TestJedis {

    /*测试redis的连接状况*/
    @Test
    public void  test(){

        //默认无参，访问本机；访问远程添加"ip地址"
        Jedis jedis = new Jedis();
        String ping = jedis.ping();
        System.out.println(ping);
    }
}
