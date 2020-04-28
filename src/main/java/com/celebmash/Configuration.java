package com.celebmash;

import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {
    private int defaultNCelebs;

    // Discord Props
    private String discordBotToken;
    private List<String> discordReacts;
    // Reddit Props
    private String redditApi;

    @SuppressWarnings("unchecked")
    public Configuration() {
        Yaml yaml = new Yaml();
        Map<String, Object> props = yaml
                .load(Configuration.class.getClassLoader().getResourceAsStream("application-props.yml"));
        // App Config
        Map<String, Object> appProps = (Map<String, Object>) props.get("app");
        this.setDefaultNCelebs((Integer) appProps.get("defaultNCeleb"));
        // Discord Config
        Map<String, Object> discordProps = (Map<String, Object>) props.get("discord");
        this.setDiscordBotToken((String) discordProps.get("botToken"));
        this.setDiscordReacts((List<String>) discordProps.get("reacts"));
        // Reddit Config
        Map<String, Object> redditProps = (Map<String, Object>) props.get("reddit");
        this.setRedditApi((String) redditProps.get("api"));
    }

    public int getDefaultNCelebs() {
        return this.defaultNCelebs;
    }

    public void setDefaultNCelebs(int defaultNCelebs) {
        this.defaultNCelebs = defaultNCelebs;
    }

    public String getDiscordBotToken() {
        return this.discordBotToken;
    }

    public void setDiscordBotToken(String discordBotToken) {
        this.discordBotToken = discordBotToken;
    }

    public String getRedditApi() {
        return this.redditApi;
    }

    public void setRedditApi(String redditApi) {
        this.redditApi = redditApi;
    }

    public List<String> getDiscordReacts() {
        return this.discordReacts;
    }

    public void setDiscordReacts(List<String> discordReacts) {
        this.discordReacts = discordReacts;
    }
}