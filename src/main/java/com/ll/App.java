package com.ll;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    Scanner scanner = new Scanner(System.in);
    int lastId = 0;
    List<WiseSaying> wiseSayingList = new ArrayList<>();

    public App() {
        loadData();
    }

    void run () {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equals("종료")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else  if (cmd.equals("등록")) {
                actionWrite();
            } else if (cmd.equals("목록")) {
                actionList();
            } else if (cmd.startsWith("삭제")) {
                actionDelete(cmd);
            } else if (cmd.startsWith("수정")) {
                actionModify(cmd);
            }
        }
        scanner.close();
    }

    void actionWrite() {
        System.out.print("명언: ");
        String content = scanner.nextLine().trim();

        System.out.print("작가: ");
        String author = scanner.nextLine().trim();

        WiseSaying wiseSaying = write(author, content);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    WiseSaying write (String author, String content) {
        WiseSaying wiseSaying = new WiseSaying(++lastId, author, content );

        wiseSayingList.add(wiseSaying);
        saveWiseSaying(wiseSaying);
        saveLastId();

        return wiseSaying;
    }

    void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (int i = wiseSayingList.size() - 1; i >=0; i--) {
            WiseSaying wiseSaying = wiseSayingList.get(i);
            System.out.println("%d / %s / %s".formatted(wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));
        }
    }

    void actionDelete(String cmd) {
        int id = CmdSplitId(cmd);

        if (id < 0) {
            return;
        }

        WiseSaying wiseSaying = findById(id);

        if (wiseSaying == null) {
            return;
        }

        delete(wiseSaying);

        System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
    }

    void delete(WiseSaying wiseSaying) {
        wiseSayingList.remove(wiseSaying);
        File jsonFile = new File("db/wiseSaying/" + wiseSaying.getId() + ".json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }

    void actionModify(String cmd) {
        int id = CmdSplitId(cmd);

        if (id < 0) {
            return;
        }

        WiseSaying wiseSaying = findById(id);

        if (wiseSaying == null) {
            return;
        }

        System.out.printf("명언(기존) : %s\n",  wiseSaying.getContent());
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();

        System.out.printf("작가(기존) : %s\n",  wiseSaying.getAuthor());
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        modify(wiseSaying, content, author);

        System.out.println("%d번 명언이 수정 되었습니다.".formatted(id));
    }

    void modify(WiseSaying wiseSaying, String content, String author) {
        wiseSaying.setContent(content);
        wiseSaying.setAuthor(author);
        saveWiseSaying(wiseSaying);
    }

    WiseSaying findById(int id) {
        WiseSaying wiseSaying = null;
        for (int i = 0; i < wiseSayingList.size(); i++) {
            if (wiseSayingList.get(i).getId() == id) {
                wiseSaying = wiseSayingList.get(i);
            }
        }

        if (wiseSaying == null) {
            System.out.println("해당 아이디는 존재하지 않습니다.");
            return null;
        }

        return wiseSaying;
    }

    int CmdSplitId(String cmd) {
        String[] cmdBits = cmd.split("=");

        if (cmdBits.length < 2 ||  cmdBits[1].isEmpty()) {
            System.out.println("id를 입력해주세요.");
            return -1;
        }

        try {
            return Integer.parseInt(cmdBits[1]);
        } catch (NumberFormatException e) {
            System.out.println("유효한 id를 입력해주세요.");
            return -1;
        }
    }

    private void loadData() {
        File dir = new File("db/wiseSaying");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File lastIdFile = new File("db/wiseSaying/lastId.txt");
        if (lastIdFile.exists()) {
            try (Scanner sc = new Scanner(lastIdFile)) {
                if (sc.hasNextInt()) {
                    lastId = sc.nextInt();
                }
            }
        }

        Pattern pattern = Pattern.compile("\\{\\s*\"id\":\\s*(\\d+),\\s*\"content\":\\s*\"(.*)\",\\s*\"author\":\\s*\"(.*)\"\\s*\\}");

        for (int i = 1; i <= lastId; i++) {
            File jsonFile = new File("db/wiseSaying/" + i + ".json");
            if (jsonFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line.trim());
                    }
                    String json = sb.toString();

                    Matcher matcher = pattern.matcher(json);
                    if (matcher.matches()) {
                        int id = Integer.parseInt(matcher.group(1));
                        String content = matcher.group(2);
                        String author = matcher.group(3);
                        wiseSayingList.add(new WiseSaying(id, author, content));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveWiseSaying(WiseSaying wiseSaying) {
        String json = String.format("{\n  \"id\": %d,\n  \"content\": \"%s\",\n  \"author\": \"%s\"\n}",
                wiseSaying.getId(),
                wiseSaying.getContent().replace("\"", "\\\""),
                wiseSaying.getAuthor().replace("\"", "\\\""));

        File jsonFile = new File("db/wiseSaying/" + wiseSaying.getId() + ".json");
        try (FileWriter fw = new FileWriter(jsonFile)) {
            fw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLastId() {
        File lastIdFile = new File("db/wiseSaying/lastId.txt");
        try (FileWriter fw = new FileWriter(lastIdFile)) {
            fw.write(String.valueOf(lastId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}