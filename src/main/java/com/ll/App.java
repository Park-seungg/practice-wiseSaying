// src/main/java/com/ll/App.java
package com.ll;

import com.ll.domain.system.controller.SystemController;
import com.ll.domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    private final Scanner scanner;

    public App(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        SystemController systemController = new SystemController();
        WiseSayingController wiseSayingController = new WiseSayingController(scanner);

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = scanner.nextLine().trim();

            if (command.isEmpty()) continue;

            Rq rq = new Rq(command);

            switch (rq.getAction()) {
                case "종료":
                    systemController.exit();
                    return;
                case "빌드":
                    systemController.build();
                    break;
                case "등록":
                    wiseSayingController.write();
                    break;
                case "목록":
                    wiseSayingController.list(rq);
                    break;
                case "삭제":
                    wiseSayingController.delete(rq);
                    break;
                case "수정":
                    wiseSayingController.modify(rq);
                    break;
                default:
                    System.out.println("알 수 없는 명령입니다.");
            }
        }
    }
}