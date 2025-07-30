package com.ll;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else  if (cmd.equals("등록")) {
                System.out.print("명언: ");
                String wiseSayingContent = scanner.nextLine().trim();

                System.out.print("작가: ");
                String wiseSayingAuthor = scanner.nextLine().trim();
            }
        }

        scanner.close();
    }
}