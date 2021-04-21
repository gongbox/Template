package com.gongbo.template.controller;

import com.gongbo.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("test")
public class TestController {

    @GetMapping("hello")
    public Result<String> hello() {
        return Result.success("hello");
    }
    @GetMapping("nullTest")
    public Result<String> nullTest() {
        throw new NullPointerException();
    }
}
