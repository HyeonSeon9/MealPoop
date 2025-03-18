package com.sheepdeny.mealpoop.dnf.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum ServerName {
    ANTON("안톤"),
    BAKAL("바칼"),
    CAIN("카인"),
    CASILLAS("카시야스"),
    DIREGIE("디레지에"),
    HILDER("힐더"),
    PREY("프레이"),
    SIROCO("시로코");

    private final String koreanName;
    private static final Map<String, ServerName> KOREAN_TO_ENUM_MAP;

    static {
        KOREAN_TO_ENUM_MAP = Arrays.stream(ServerName.values())
                .collect(Collectors.toMap(ServerName::getKoreanName, Function.identity()));
    }

    ServerName(String koreanName) {
        this.koreanName = koreanName;
    }

    public static String findEnglishNameByKorean(String koreanName) {
        ServerName server = KOREAN_TO_ENUM_MAP.get(koreanName);
        return (server != null) ? server.name().toLowerCase() : null;
    }
}
