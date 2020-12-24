package com.tamastudy.jon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // View 를 Return 하겠다 !
public class IndexController {
    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) but 생략가능 !
        return "index";
    }
}
