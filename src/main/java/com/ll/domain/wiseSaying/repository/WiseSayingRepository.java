package com.ll.domain.wiseSaying.repository;

import com.ll.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiseSayingRepository {
    private static final String BASE_DIR = "db/wiseSaying";
    private static final String LAST_ID_FILE = BASE_DIR + "/lastId.txt";
    private static final String DATA_JSON = "data.json";

    private long lastId;

    public WiseSayingRepository() {
        createDirectories();
        loadLastId();
    }

    private void createDirectories() {
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void loadLastId() {
        File file = new File(LAST_ID_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                lastId = Long.parseLong(br.readLine().trim());
            } catch (IOException | NumberFormatException e) {
                lastId = 0;
            }
        } else {
            lastId = 0;
        }
    }

    private void saveLastId() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LAST_ID_FILE))) {
            bw.write(String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long write(String content, String author) {
        lastId++;
        WiseSaying wiseSaying = new WiseSaying(lastId, content, author);
        saveToFile(wiseSaying);
        saveLastId();
        return lastId;
    }

    private void saveToFile(WiseSaying wiseSaying) {
        String filePath = BASE_DIR + "/" + wiseSaying.getId() + ".json";
        String json = String.format("{\n  \"id\": %d,\n  \"content\": \"%s\",\n  \"author\": \"%s\"\n}",
                wiseSaying.getId(), escapeJson(wiseSaying.getContent()), escapeJson(wiseSaying.getAuthor()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String escapeJson(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public List<WiseSaying> findAll() {
        List<WiseSaying> wiseSayings = new ArrayList<>();
        for (long i = 1; i <= lastId; i++) {
            WiseSaying ws = findById(i);
            if (ws != null) {
                wiseSayings.add(ws);
            }
        }
        return wiseSayings;
    }

    public WiseSaying findById(long id) {
        String filePath = BASE_DIR + "/" + id + ".json";
        if (!new File(filePath).exists()) {
            return null;
        }
        try {
            String json = Files.readString(Paths.get(filePath));

            Pattern contentPattern = Pattern.compile("\"content\": \"(.*?)\"");
            Matcher contentMatcher = contentPattern.matcher(json);
            String content = contentMatcher.find() ? contentMatcher.group(1) : "";

            Pattern authorPattern = Pattern.compile("\"author\": \"(.*?)\"");
            Matcher authorMatcher = authorPattern.matcher(json);
            String author = authorMatcher.find() ? authorMatcher.group(1) : "";

            return new WiseSaying(id, content, author);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(long id) {
        String filePath = BASE_DIR + "/" + id + ".json";
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }

    public void modify(long id, String content, String author) {
        WiseSaying wiseSaying = findById(id);
        if (wiseSaying != null) {
            wiseSaying.setContent(content);
            wiseSaying.setAuthor(author);
            saveToFile(wiseSaying);
        }
    }

    public void build() {
        List<WiseSaying> wiseSayings = findAll();
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < wiseSayings.size(); i++) {
            WiseSaying ws = wiseSayings.get(i);
            jsonBuilder.append(String.format("  {\n    \"id\": %d,\n    \"content\": \"%s\",\n    \"author\": \"%s\"\n  }",
                    ws.getId(), escapeJson(ws.getContent()), escapeJson(ws.getAuthor())));
            if (i < wiseSayings.size() - 1) {
                jsonBuilder.append(",\n");
            } else {
                jsonBuilder.append("\n");
            }
        }
        jsonBuilder.append("]");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_JSON))) {
            bw.write(jsonBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long getLastId() {
        return lastId;
    }
}