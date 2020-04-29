package com.qfedu;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther Zhangbo
 * @date 2020/4/28 18:39
 */
/*jedis的基本操作*/
public class TestJedis {

    /*测试redis的连接状况*/
    @Test
    public void  test1(){

        //默认无参，访问本机；访问远程添加"ip地址"
        Jedis jedis = new Jedis();
        String ping = jedis.ping();
        System.out.println(ping);
    }


    Jedis j = null;

    //测试方法执行前：创建连接
    @Before
    public void setup(){
        j = new Jedis();
    }

    //测试方法执行后：关闭连接
    @After
    public void  tearDown(){
        if(j != null){
            j.close();
            j = null;
        }
    }

    //String的基本使用
    @Test
    public  void  testString(){
        String set = j.set("aaa", "bbb");
        System.out.println(set);
        String get = j.get("aaa");
        System.out.println(get);
    }

    //hash对象的基本操作
    @Test
    public void testHash(){
        Map<String,String> actors = new HashMap<>();
        actors.put("jack","jack value");
        actors.put("rose","rose value");

        String hmset = j.hmset("actor", actors);
        System.out.println(hmset);

        //根据key获取全部对象值
        Map<String, String> hgetAll = j.hgetAll("actor");
        System.out.println(hgetAll);
        //根据key和属性，获取属相的值
        String hget = j.hget("actor", "jack");
        System.out.println(hget);
    }

    //list的基本操作:有序重复,返回下标索引
    @Test
    public  void  testList(){
        //观察返回索引
        System.out.println(j.rpush("person","xiaobai"));
        System.out.println(j.rpush("person","xiaohong"));
        System.out.println(j.rpush("person","xiaohei","xiaolan","xiaobai"));
        //观察添加顺序和重复
        System.out.println(j.lrange("person",0,10));
    }

    //set的基本操作:无序去重，返回影响的记录数
    @Test
    public void  testSet(){
        //观察返回影响数
        System.out.println(j.sadd("food","baicai","qingcai","baicai"));
        //观察无序和重复数
        System.out.println(j.smembers("food"));
    }

    //zset的基本操作：有序去重，value相同时替换索引值
    @Test
    public void testZSet(){
        //观察有序去重的问题
        System.out.println(j.zadd("fenshu",10,"java"));
        System.out.println(j.zadd("fenshu",99,"html5"));
        System.out.println(j.zadd("fenshu",60,"math"));

        //观察value相同时，索引值替换的状况
        //System.out.println(j.zadd("fenshu",80,"java"));

        System.out.println(j.zrangeByScore("fenshu", 60, 100));
    }

    //jedis连接池的解决
    @Test
    public void testPool(){
        //jedis连接池设置
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(20);  //设置最大的空闲数
        config.setMaxTotal(50); //设置最大的连接总数
        config.setMaxWaitMillis(5000);  //设置等待时间

        //redis连接信息
        ArrayList<JedisShardInfo> shard = new ArrayList<>();
        shard.add(new JedisShardInfo("localhost",6379));
      //shard.add(new JedisShardInfo("localhost",6378)); //可以连接多个服务器

        //获取连接池对象
        ShardedJedisPool pool = new ShardedJedisPool(config,shard);
        ShardedJedis jedis = pool.getResource();

        //使用连接池对象进行基本操作
        System.out.println(jedis.get("aaa"));
    }

}
