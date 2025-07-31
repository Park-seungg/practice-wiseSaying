package com.ll;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Rq {
    private String action;
    private Map<String, String> params;

    public Rq(String command) {
        params = new HashMap<>();

        String[] commandParts = command.split("\\?", 2);
        action = commandParts[0].trim();

        if (commandParts.length > 1) {
            String paramsStr = commandParts[1];
            params = Arrays.stream(paramsStr.split("&"))
                    .map(paramPair -> paramPair.split("=", 2))
                    .filter(paramPairParts -> paramPairParts.length == 2)
                    .collect(Collectors.toMap(
                            paramPairParts -> paramPairParts[0].trim(),
                            paramPairParts -> paramPairParts[1].trim()
                    ));
        }
    }

    public String getAction() {
        return action;
    }

    public String getParam(String name, String defaultValue) {
        return params.getOrDefault(name, defaultValue);
    }

    public int getIntParam(String name, int defaultValue) {
        try {
            return Integer.parseInt(getParam(name, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public long getLongParam(String name, long defaultValue) {
        try {
            return Long.parseLong(getParam(name, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}