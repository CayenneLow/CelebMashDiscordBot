package com.celebmash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class Configuration {
    private int defaultNCelebs;

    // Discord Props
    private String discordBotToken;
    private List<Pair<String, String>> discordReacts = new ArrayList<>(); // left: Discord format, right: unicode
    // Reddit Props
    private String redditApi;
    private String redditApiNSFW;

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
        List<String> reacts = (List<String>) discordProps.get("reacts");
        for (String react : reacts) {
            Pair<String, String> pair = new ImmutablePair<String, String>(react.split(" ")[0], react.split(" ")[1]);
            this.discordReacts.add(pair);
        }
        if (defaultNCelebs > reacts.size())
            throw new RuntimeException("Too many celebs requested, slow down there thirsty boi");
        // Reddit Config
        Map<String, Object> redditProps = (Map<String, Object>) props.get("reddit");
        this.setRedditApi((String) redditProps.get("api"));
        this.setRedditApiNSFW((String) redditProps.get("apiNsfw"));
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

    public List<Pair<String, String>> getDiscordReacts() {
        return this.discordReacts;
    }

    public void setDiscordReacts(List<Pair<String, String>> discordReacts) {
        this.discordReacts = discordReacts;
    }

    public String getRedditApiNSFW() {
        return this.redditApiNSFW;
    }

    public void setRedditApiNSFW(String redditApiNSFW) {
        this.redditApiNSFW = redditApiNSFW;
    }

}