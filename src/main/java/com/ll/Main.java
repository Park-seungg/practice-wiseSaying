package com.ll;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");

        Scanner scanner = new Scanner(System.in);
        WiseSaying wiseSaying = null;
        int lastId = 0;
        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else if (cmd.equals("등록")) {
                System.out.print("명언: ");
                String wiseSayingContent = scanner.nextLine().trim();

                System.out.print("작가: ");
                String wiseSayingAuthor = scanner.nextLine().trim();

                int id = ++lastId;

                wiseSaying = new WiseSaying();

                wiseSaying.id = id;
                wiseSaying.author = wiseSayingAuthor;
                wiseSaying.content = wiseSayingContent;

                System.out.println("%d번 명언이 등록되었습니다.".formatted(id));
            } else if (cmd.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                System.out.println("%d / %s / %s".formatted(wiseSaying.id, wiseSaying.author, wiseSaying.content));
            }
        }

        scanner.close();
    }
}