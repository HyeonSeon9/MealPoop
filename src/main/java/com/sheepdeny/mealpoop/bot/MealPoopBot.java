package com.sheepdeny.mealpoop.bot;


import com.sheepdeny.mealpoop.bot.command.Command;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MealPoopBot {

    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private List<Command> commands;

    private final Map<String, Command> commandMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (Command command : commands) {
            commandMap.put(command.getName(), command);
        }

        client.on(MessageCreateEvent.class)
                .flatMap(this::handle)
                .subscribe();
    }

    private Mono<Void> handle(MessageCreateEvent event) {
        Message message = event.getMessage();
        String content = message.getContent();

        if (!content.startsWith("!")) {
            return Mono.empty();
        }

        String[] parts = content.substring(1).split(" ", 2);
        String commandName = parts[0].toLowerCase();
        Command command = commandMap.get(commandName);

        if (command != null) {
            return command.execute(event);
        }

        return Mono.empty();
    }
}
