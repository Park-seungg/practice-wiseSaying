package com.ll.domain.wiseSaying.service;

import com.ll.domain.wiseSaying.entity.WiseSaying;
import com.ll.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;
import java.util.stream.Collectors;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;
    private static boolean testMode = false;

    public static void setTestMode(boolean testMode) {
        WiseSayingService.testMode = testMode;
    }

    public WiseSayingService() {
        this.wiseSayingRepository = new WiseSayingRepository();
        ensureSampleData();
    }

    private void ensureSampleData() {
        if (wiseSayingRepository.getLastId() == 0 && !testMode) {
            for (int i = 1; i <= 10; i++) {
                wiseSayingRepository.write("명언 " + i, "작자미상 " + i);
            }
        }
    }

    public long write(String content, String author) {
        return wiseSayingRepository.write(content, author);
    }

    public List<WiseSaying> findAll(String keywordType, String keyword, int page, int pageSize) {
        List<WiseSaying> all = wiseSayingRepository.findAll().stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .collect(Collectors.toList());

        if (!keyword.isEmpty()) {
            all = all.stream()
                    .filter(ws -> {
                        if ("content".equals(keywordType)) {
                            return ws.getContent().contains(keyword);
                        } else if ("author".equals(keywordType)) {
                            return ws.getAuthor().contains(keyword);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, all.size());

        if (from >= all.size()) {
            return new java.util.ArrayList<>();
        }

        return all.subList(from, to);
    }

    public int getTotalPages(String keywordType, String keyword, int pageSize) {
        int totalItems = findAll(keywordType, keyword, 1, Integer.MAX_VALUE).size();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public boolean remove(long id) {
        return wiseSayingRepository.remove(id);
    }

    public WiseSaying findById(long id) {
        return wiseSayingRepository.findById(id);
    }

    public void modify(long id, String content, String author) {
        wiseSayingRepository.modify(id, content, author);
    }

    public void build() {
        wiseSayingRepository.build();
    }
}