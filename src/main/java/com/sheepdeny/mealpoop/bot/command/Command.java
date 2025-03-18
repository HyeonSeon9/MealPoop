package com.sheepdeny.mealpoop.bot.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.List;

public interface Command {

    String getName();
    List<String> getDescriptions();
    Mono<Void> execute(MessageCreateEvent event);
}
