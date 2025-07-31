package com.ll;

import com.ll.domain.wiseSaying.service.WiseSayingService;
import com.ll.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String input, boolean testMode) {
        WiseSayingService.setTestMode(testMode);
        Scanner scanner = TestUtil.genScanner(input + "\n종료");
        ByteArrayOutputStream byteArrayOutputStream = TestUtil.setOutToByteArray();

        new App(scanner).run();

        String out = byteArrayOutputStream.toString().trim();
        TestUtil.clearSetOutToByteArray();

        return out;
    }
}