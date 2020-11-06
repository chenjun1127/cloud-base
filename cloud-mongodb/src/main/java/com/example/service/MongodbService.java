package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entities.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: cj
 * @Date: 2020/11/6 10:47
 */
@Service
public class MongodbService {
    @Resource
    private MongoTemplate mongoTemplate;

    /*
     * @Description 保存对象
     * @Param [user]
     * @return java.lang.String
     */
    public String saveUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        mongoTemplate.save(user);
        return "添加成功";
    }

    /*
     * @Description 查询所有
     * @Param []
     * @return java.util.List<com.example.entities.User>
     */
    public List<User> fillAllUser() {
        return mongoTemplate.findAll(User.class);
    }

    /*
     * @Description: 根据ID查询
     * @Param [id]
     * @return com.example.entities.User
     */
    public User getUserById(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class);
    }

    /*
     * @Description: 根据名称查询
     * @Param [name]
     * @return com.example.entities.User
     */
    public User getUserByName(String name) {
        return mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), User.class);
    }

    /*
     * @Description: 根据ID更新，入参为json,必须要有id
     * @Param [user]
     * @return java.lang.String
     */
    public String updateUser(User user) {
        if (StrUtil.isBlank(user.getId())) return "id不存在";
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update().set("name", user.getName()).set("sex", user.getSex()).set("updateTime", new Date());
        // updateFirst 更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, User.class);
        // updateMulti 更新查询返回结果集的全部
        // mongoTemplate.updateMulti(query, update, User.class);
        // upsert 更新对象不存在则去添加
        // mongoTemplate.upsert(query, update, User.class);
        return "更新成功";
    }

    /*
     * @Description: 删除对象
     * @Param [user]
     * @return java.lang.String
     */
    public String delUser(User user) {
        mongoTemplate.remove(user);
        return "删除成功";
    }

    /*
     * @Description: 根据ID删除
     * @Param [id]
     * @return java.lang.String
     */
    public String delUserById(String id) {
        mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), User.class);
        return "删除成功";
    }

    /*
     * @Description: 模糊查询
     * @Param [search]
     * @return java.util.List<com.example.entities.User>
     */
    public List<User> findByLikes(String search) {
        Query query = new Query();
        //criteria.where("name").regex(search);
        Pattern pattern = Pattern.compile("^.*" + search + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria.where("name").regex(pattern);
        return mongoTemplate.findAllAndRemove(query, User.class);
    }

}
