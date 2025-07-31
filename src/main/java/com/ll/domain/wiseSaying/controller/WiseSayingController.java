package com.ll.domain.wiseSaying.controller;

import com.ll.Rq;
import com.ll.domain.wiseSaying.entity.WiseSaying;
import com.ll.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner scanner;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner scanner) {
        this.scanner = scanner;
        this.wiseSayingService = new WiseSayingService();
    }

    public void write() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        long id = wiseSayingService.write(content, author);

        System.out.printf("%d번 명언이 등록되었습니다.\n", id);
    }

    public void list(Rq rq) {
        String keywordType = rq.getParam("keywordType", "");
        String keyword = rq.getParam("keyword", "");
        int page = rq.getIntParam("page", 1);
        int pageSize = 5;

        if (!keyword.isEmpty()) {
            System.out.println("----------------------");
            System.out.printf("검색타입 : %s\n", keywordType);
            System.out.printf("검색어 : %s\n", keyword);
            System.out.println("----------------------");
        }

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayings = wiseSayingService.findAll(keywordType, keyword, page, pageSize);

        for (WiseSaying wiseSaying : wiseSayings) {
            System.out.printf("%d / %s / %s\n", wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent());
        }

        System.out.println("----------------------");

        int totalPages = wiseSayingService.getTotalPages(keywordType, keyword, pageSize);
        System.out.printf("페이지 : %s\n", getPageDisplay(page, totalPages));
    }

    private String getPageDisplay(int currentPage, int totalPages) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= totalPages; i++) {
            if (i > 1) {
                sb.append(" / ");
            }
            if (i == currentPage) {
                sb.append("[").append(i).append("]");
            } else {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public void delete(Rq rq) {
        long id = rq.getLongParam("id", 0);

        if (id == 0) {
            System.out.println("id를 입력해주세요.");
            return;
        }

        boolean removed = wiseSayingService.remove(id);

        if (removed) {
            System.out.printf("%d번 명언이 삭제되었습니다.\n", id);
        } else {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
        }
    }

    public void modify(Rq rq) {
        long id = rq.getLongParam("id", 0);

        if (id == 0) {
            System.out.println("id를 입력해주세요.");
            return;
        }

        WiseSaying wiseSaying = wiseSayingService.findById(id);

        if (wiseSaying == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", id);
            return;
        }

        System.out.printf("명언(기존) : %s\n", wiseSaying.getContent());
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();

        System.out.printf("작가(기존) : %s\n", wiseSaying.getAuthor());
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        wiseSayingService.modify(id, content, author);
    }
}