package com.sheepdeny.mealpoop.dnf.command;

import com.sheepdeny.mealpoop.bot.command.Command;
import com.sheepdeny.mealpoop.dnf.common.ServerName;
import com.sheepdeny.mealpoop.dnf.dto.CharacterInfo;
import com.sheepdeny.mealpoop.dnf.exception.NotFoundServerName;
import com.sheepdeny.mealpoop.dnf.service.DungeonAndFighterService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DungeonAndFighterCommand implements Command {

    private final DungeonAndFighterService service;
    private final String commandPrefix;

    @Override
    public String getName() {
        return "dnf";
    }

    @Override
    public List<String> getDescriptions() {
        return List.of(
                String.format("`%s%s 캐릭터 <서버명-한글> <캐릭터명>` - 캐릭터 정보를 조회합니다", commandPrefix, getName())
        );
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        Message message = event.getMessage();
        String content = message.getContent();
        Mono<MessageChannel> channelMono = message.getChannel();

        String[] parts = content.substring(1).split(" ");
        String command = parts[1];

        switch (command){
            case "캐릭터":
                String server = parts[2];
                String characterName = parts[3];

                return service.getCharacterMono(server, characterName)
                        .flatMap(response -> {
                            if (response.getRows().isEmpty()) {
                                return channelMono.flatMap(channel ->
                                        channel.createMessage("❌ 해당 캐릭터를 찾을 수 없습니다.")
                                );
                            }
                            CharacterInfo character = response.getRows().get(0);
//                            String userMention = message.getAuthor()
//                                    .map(User::getMention)
//                                    .orElse("Unknown User");

                            return channelMono.flatMap(channel ->
                                    channel.createMessage(createCharacterEmbed(character))
                            );
                        })
                        .onErrorResume(NotFoundServerName.class, e ->
                                channelMono.flatMap(channel ->
                                        channel.createMessage("❌ 존재하지 않는 서버명입니다.")
                                )
                        )
                        .onErrorResume(Exception.class, e -> {
                            log.error("캐릭터 정보를 불러오는 중 오류 발생", e); // 로깅 추가
                            return channelMono.flatMap(channel ->
                                    channel.createMessage("⚠️ 캐릭터 정보를 불러오는 중 오류가 발생했습니다. 나중에 다시 시도해 주세요.")
                            );
                        })
                        .then();
            default:
                return Mono.empty();
        }
    }


    private EmbedCreateSpec createCharacterEmbed(CharacterInfo character) {

        String imageUrl = String.format(
                "https://img-api.neople.co.kr/df/servers/%s/characters/%s?zoom=3",
                character.getServerId(),
                character.getCharacterId()
        );

        return EmbedCreateSpec.builder()
                .color(Color.BLUE)
                .title("캐릭터 정보")
                .description("캐릭터 정보를 조회합니다")
                .addField("서버", ServerName.valueOf(character.getServerId().toUpperCase()).getKoreanName(), true)
                .addField("캐릭터 이름", character.getCharacterName(), true)
                .addField("직업", character.getJobGrowName(), true)
                .addField("명성", String.valueOf(character.getFame()),true)
                .image(imageUrl)
                .footer("제공: 현숭", null)
                .build();
    }

}
