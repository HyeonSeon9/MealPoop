package com.sheepdeny.mealpoop.dnf.service;

import com.sheepdeny.mealpoop.dnf.common.ServerName;
import com.sheepdeny.mealpoop.dnf.dto.CharactersResponse;
import com.sheepdeny.mealpoop.dnf.exception.NotFoundServerName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DungeonAndFighterService {
    @Value("${dnf.token}")
    private String apikey;

    @Value("${dnf.api.url}")
    private String apiUrl;

    private final WebClient.Builder webClientBuilder;

    public Mono<CharactersResponse> getCharacterMono(String serverName, String characterName) {
        String engServerName = ServerName.findEnglishNameByKorean(serverName);

        if (Objects.isNull(engServerName)) {
            return Mono.error(new NotFoundServerName("정확한 서버 이름을 입력하세요"));
        }

        String requestUrl = apiUrl + "/df/servers/" + engServerName + "/characters?characterName=" + characterName + "&apikey=" + apikey;

        return webClientBuilder.build()
                .get()
                .uri(requestUrl)
                .retrieve()
                .bodyToMono(CharactersResponse.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("⚠️ API 호출 중 오류가 발생했습니다.")));
    }
}
