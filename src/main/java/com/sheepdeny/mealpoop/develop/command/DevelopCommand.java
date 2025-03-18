package com.sheepdeny.mealpoop.develop.command;

import com.sheepdeny.mealpoop.bot.command.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DevelopCommand implements Command {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public List<String> getDescriptions() {
        return List.of(
                "`!ping` - pong!"
        );
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage("Pong!"))
                .then();
    }
}
