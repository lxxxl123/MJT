package org.example.chen.controller;


import org.example.chen.http.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;


@RestController
public class TestController {


    @GetMapping("/test/systime")
    public ResponseResult<String> systime(){
        return ResponseResult.success(Calendar.getInstance().toInstant().toString());
    }
}
