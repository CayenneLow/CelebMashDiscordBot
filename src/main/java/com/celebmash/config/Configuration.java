package com.celebmash.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class Configuration {
    private static Logger log = LoggerFactory.getLogger(Configuration.class);
    private DiscordProps discord;
    private RedditProps reddit;

    private static Configuration instance = null;

    private AppProps app;

    public Configuration() {
    }

    public Configuration(DiscordProps discord, RedditProps reddit, AppProps app) {
        this.discord = discord;
        this.reddit = reddit;
        this.app = app;
    }

    public static Configuration load(boolean isProd) {
        if (instance == null) {
            Dotenv dotenv = Dotenv.load();
            // Discord
            DiscordProps discordProps = new DiscordProps();
            discordProps.setBotToken(dotenv.get("DISCORD_BOT_TOKEN"));
            Map<String, String> reactionMap = new HashMap<>();
            for (String r : Arrays.asList(dotenv.get("REACTS").split(","))) {
                String reaction = r.split(":")[0];
                String unicode = r.split(":")[1];
                reactionMap.put(reaction, unicode);
            }
            discordProps.setReacts(reactionMap);
            log.info(reactionMap.toString());

            // Reddit
            RedditProps redditProps = new RedditProps();
            redditProps.setBaseUrl(dotenv.get("BASE_URL"));
            redditProps.setSource(dotenv.get("SOURCE_SUBREDDIT"));
            redditProps.setSourceNsfw(dotenv.get("SOURCE_NSFW_SUBREDDIT"));
            redditProps.setRefreshTokenUrl(dotenv.get("REFRESH_TOKEN_URL"));
            redditProps.setRefreshToken(dotenv.get("REFRESH_TOKEN"));
            redditProps.setOAuthHeader(dotenv.get("OAUTH_HEADER"));
            redditProps.setRefreshAuthHeader(dotenv.get("REFRESH_AUTH_HEADER"));

            // App
            AppProps appProps = new AppProps();
            appProps.setDefaultNCeleb(Integer.parseInt(dotenv.get("DEFAULT_N_CELEB")));

            instance = new Configuration(discordProps, redditProps, appProps);
        } 
        return instance;
    }

    public void refreshAccessToken(String newAccessToken) {
        // refreshing in memory
        reddit.setOAuthHeader("Bearer " + newAccessToken);
    }

    public DiscordProps getDiscord() {
        return this.discord;
    }

    public void setDiscord(DiscordProps discord) {
        this.discord = discord;
    }

    public RedditProps getReddit() {
        return this.reddit;
    }

    public void setReddit(RedditProps reddit) {
        this.reddit = reddit;
    }

    public AppProps getApp() {
        return this.app;
    }

    public void setApp(AppProps app) {
        this.app = app;
    }
}