package com.example.controller;

import com.example.entities.User;
import com.example.service.MongodbService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: cj
 * @Date: 2020/11/6 11:16
 */
@RestController
@RequestMapping("/mongo")
public class MongodbController {
    @Resource
    private MongodbService mongodbService;

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User user) {
        return mongodbService.saveUser(user);
    }

    @GetMapping("/fillAllUser")
    public List<User> saveUser() {
        return mongodbService.fillAllUser();
    }

    @GetMapping("/getUserById")
    public User getUserById(String id) {
        return mongodbService.getUserById(id);
    }

    @GetMapping("/getUserByName")
    public User getUserByName(String name) {
        return mongodbService.getUserByName(name);
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody User user) {
        return mongodbService.updateUser(user);
    }

    @PostMapping("/delUser")
    public String delUser(@RequestBody User user) {
        return mongodbService.delUser(user);
    }

    @GetMapping("/delUserById")
    public String delUserById(String id) {
        return mongodbService.delUserById(id);
    }

    @GetMapping("/findByLikes")
    public List<User> findByLikes(String search) {
        return mongodbService.findByLikes(search);
    }
}
