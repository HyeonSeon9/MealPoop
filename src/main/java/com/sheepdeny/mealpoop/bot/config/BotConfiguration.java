package com.sheepdeny.mealpoop.bot.config;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {
    @Value("${discord.bot.token}")
    private String token;

    @Value("${discord.bot.prefix}")
    private String commandPrefix;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        return DiscordClient.create(token)
                .login()
                .block();
    }

    @Bean
    public String commandPrefix() {
        return commandPrefix;
    }
}
