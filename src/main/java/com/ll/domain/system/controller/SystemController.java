package com.ll.domain.system.controller;

import com.ll.domain.wiseSaying.service.WiseSayingService;

public class SystemController {

    public void exit() {
        System.out.println("프로그램을 종료합니다.");
    }

    public void build() {
        new WiseSayingService().build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}