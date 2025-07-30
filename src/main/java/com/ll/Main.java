package com.ll;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("== 명언 앱 ==");
        Scanner scanner = new Scanner(System.in);
        int wiseSayingId = 0;

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();
            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다");
                break;
            } else if (cmd.equals("등록")) {
                System.out.print("명언 : ");
                String wise = scanner.nextLine().trim();
                System.out.print("작가 : ");
                String auth = scanner.nextLine().trim();
                wiseSayingId++;
                System.out.println(wiseSayingId + "번 명언이 등록되었습니다.");
            }
        }
    }
}