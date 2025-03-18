package com.sheepdeny.mealpoop.bot.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command{

    private final List<Command> commands;

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getDescriptions() {
        return List.of(
                "사용 가능한 모든 커맨드와 사용법을 표시합니다. 사용법 : `!help`"
        );
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title("📜 사용 가능한 커맨드 목록")
                .description("아래는 이 봇에서 사용할 수 있는 커맨드 목록입니다.")
                .color(Color.CYAN)
                .footer("궁금한 점은 !help로 확인하세요!", null);

        for (Command cmd : commands) {
            String desc = String.join("\n", cmd.getDescriptions());
            embedBuilder.addField("!" + cmd.getName(), desc, false);
        }

        EmbedCreateSpec embed = embedBuilder.build();

        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage(embed))
                .then();
    }
}
