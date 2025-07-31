package com.ll;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    private String action;
    private Map<String, String> params;

    public Rq(String command) {
        params = new HashMap<>();

        String[] commandParts = command.split("\\?", 2);
        action = commandParts[0].trim();

        if (commandParts.length > 1) {
            String paramsStr = commandParts[1];
            String[] paramsPairs = paramsStr.split("&");

            for (String paramPair : paramsPairs) {
                String[] paramPairParts = paramPair.split("=", 2);
                if (paramPairParts.length == 2) {
                    String key = paramPairParts[0].trim();
                    String value = paramPairParts[1].trim();
                    params.put(key, value);
                }
            }
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