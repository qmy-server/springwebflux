package com.example.webfux.controller;

import com.example.webfux.entity.User;
import com.example.webfux.service.FaceControlService;
import com.example.webfux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FaceControlService faceControlService;


    @PostMapping("")
    public Mono<User> save(@RequestBody User user) {
        return this.userService.save(user);
    }

    @DeleteMapping("/{username}")
    public Mono<Long> deleteByUsername(@PathVariable String username) {
        return this.userService.deleteByUsername(username);
    }

    @GetMapping("/{username}")
    public Mono<User> findByUsername(@PathVariable String username) {
        return this.userService.findByUsername(username);
    }
    //基于流
    @GetMapping(value = "",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> findAll() {

        return this.userService.findAll();
        //return this.faceControlService.findAllFace();
    }
    //基于sse
    @RequestMapping(value = "/r",produces = "text/event-stream;charset=UTF-8")
    public double retrieve() {
        try {
            //每0.5秒刷新数据
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //模拟股票实时变动数据
        return Math.random();
    }

}