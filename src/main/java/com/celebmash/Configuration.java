package com.celebmash;

import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {
    public String discordBotToken;

    public Configuration() {
        Yaml yaml = new Yaml();
        Map<String, String> props = yaml.load(Configuration.class.getClassLoader().getResourceAsStream("application-props.yml"));
        this.setDiscordBotToken(props.get("discordBotToken"));
    }

    public String getDiscordBotToken() {
        return this.discordBotToken;
    }

    public void setDiscordBotToken(String discordBotToken) {
        this.discordBotToken = discordBotToken;
    }
}