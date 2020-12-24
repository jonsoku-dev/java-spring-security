package com.tamastudy.jon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View 를 Return 하겠다 !
public class IndexController {
    @GetMapping({"", "/"})
    public @ResponseBody
    String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) but 생략가능 !
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody
    String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    @GetMapping("/login") // SecurityConfig 를 만들고 나서부터 작동하지 않음.
    public @ResponseBody
    String login() {
        return "login";
    }

    @GetMapping("/join")
    public @ResponseBody
    String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody
    String joinProc() {
        return "회원가입완료됨!";
    }
}
