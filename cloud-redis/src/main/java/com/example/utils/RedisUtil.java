package com.example.utils;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: redis工具类
 * @Author: chenjun
 * @Date: 2020/11/5 12:50
 */
@Component
public class RedisUtil {

    @Resource
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*
     * @Description: 指定缓存失效时间
     * @Param: [key, time]
     * @Return: boolean
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 根据key 获取过期时间，key键不能为null，时间(秒) 返回0代表为永久有效
     * @Param: [key]
     * @Return: long
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /*
     * @Description: 判断key是否存在
     * @Param: [key]
     * @Return: boolean
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 删除缓存，key可以传一个值或多个
     * @Param: [key]
     * @Return: void
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================

    /*
     * @Description: 普通缓存获取
     * @Param: [key]
     * @Return: java.lang.Object
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /*
     * @Description: 普通缓存放入
     * @Param: [key, value]
     * @Return: boolean
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 普通缓存放入并设置时间，时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @Param: [key, value, time]
     * @Return: boolean
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 递增
     * @Param: [key, delta]
     * @Return: long
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /*
     * @Description: 递减
     * @Param: [key, delta]
     * @Return: long
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /*
     * @Description: HashGet，key键不能为null，item项不能为null
     * @Param: [key, item]
     * @Return: java.lang.Object
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /*
     * @Description: 获取hashKey对应的所有键值
     * @Param: [key]
     * @Return: java.util.Map<java.lang.Object,java.lang.Object>
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /*
     * @Description: HashSet
     * @Param: [key, map]
     * @Return: boolean
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: HashSet并设置时间
     * @Param: [key, map, time]
     * @Return: boolean
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 向一张hash表中放入数据,如果不存在将创建
     * @Param: [key, item, value]
     * @Return: boolean
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 向一张hash表中放入数据,如果不存在将创建,time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @Param: [key, item, value, time]
     * @Return: boolean
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 删除hash表中的值，key键不能为null，item项可以使多个不能为null
     * @Param: [key, item]
     * @Return: void
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /*
     * @Description: 判断hash表中是否有该项的值，key键不能为null，item项不能为null
     * @Param: [key, item]
     * @Return: boolean
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /*
     * @Description: hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @Param: [key, item, by]
     * @Return: double
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /*
     * @Description: hash递减
     * @Param: [key, item, by]
     * @Return: double
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /*
     * @Description: 根据key获取Set中的所有值
     * @Param: [key]
     * @Return: java.util.Set<java.lang.Object>
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @Description: 根据value从一个set中查询,是否存在
     * @Param: [key, value]
     * @Return: boolean
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 将数据放入set缓存
     * @Param: [key, values]
     * @Return: long
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * @Description: 将set数据放入缓存,值可以是多个
     * @Param: [key, time, values]
     * @Return: long
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * @Description: 获取set缓存的长度
     * @Param: [key]
     * @Return: long
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * @Description: 移除值为value的
     * @Param: [key, values]
     * @Return: long
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    /*
     * @Description: 获取list缓存的内容
     * @Param: [key, start, end]
     * @Return: java.util.List<java.lang.Object>
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @Description: 获取list缓存的长度
     * @Param: [key]
     * @Return: long
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * @Description: 通过索引获取list中的值,索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @Param: [key, index]
     * @Return: java.lang.Object
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @Description: 将list放入缓存
     * @Param: [key, value]
     * @Return: boolean
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 将list放入缓存
     * @Param: [key, value, time]
     * @Return: boolean
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 将list放入缓存
     * @Param: [key, value]
     * @Return: boolean
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: list放入缓存，过期时间
     * @Param: [key, value, time]
     * @Return: boolean
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 根据索引修改list中的某条数据
     * @Param: [key, index, value]
     * @Return: boolean
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * @Description: 移除N个值为value
     * @Param: [key, count, value]
     * @Return: long
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
     * @Description: 模糊查询获取key值
     * @Param: [pattern]
     * @Return: java.util.Set
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /*
     * @Description: 使用Redis的消息队列
     * @Param: [channel, message]
     * @Return: void
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    //=========BoundListOperations 用法 start============

    /*
     * @Description: 将数据添加到Redis的list中（从右边添加）
     * @Param: [listKey, time, values]
     * @Return: void
     */
    public void addToListRight(String listKey, long time, Object... values) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //插入数据
        boundValueOperations.rightPushAll(values);
        //设置过期时间
        boundValueOperations.expire(time, TimeUnit.SECONDS);
    }

    /*
     * @Description: 根据起始结束序号遍历Redis中的list，start-->起始序号 end-->结束序号
     * @Param: [listKey, start, end]
     * @Return: java.util.List<java.lang.Object>
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }

    /*
     * @Description: 弹出右边的值 --- 并且移除这个值
     * @Param: [listKey]
     * @Return: java.lang.Object
     */
    public Object rifhtPop(String listKey) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    //=========BoundListOperations 用法 End============
}