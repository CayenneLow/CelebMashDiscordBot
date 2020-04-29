package com.celebmash;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class Configuration {
    private static Logger log = LoggerFactory.getLogger(Configuration.class);
    private int defaultNCelebs;

    // Discord Props
    private String discordBotToken;
    private List<Pair<String, String>> discordReacts = new ArrayList<>(); // left: Discord format, right: unicode
    // Reddit Props
    private String baseApi;
    private String source;
    private String sourceNSFW;
    private String meUrl;
    private String authHeader;
    private String refreshTokenUrl;
    private String refreshTokenData;
    private String accessToken;
    private String refreshToken;
    private String appClientID;
    private String appClientSecret;

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
        this.setBaseApi((String) redditProps.get("baseApi"));
        this.setSource((String) redditProps.get("source"));
        this.setSourceNSFW((String) redditProps.get("sourceNsfw"));
        this.setMeUrl((String) redditProps.get("me"));
        this.setAccessToken((String) redditProps.get("accessToken"));
        this.setRefreshToken((String) redditProps.get("refreshToken"));
        this.setAppClientID((String) redditProps.get("appClientID"));
        this.setAppClientSecret((String) redditProps.get("appClientSecret"));
        this.setAuthHeader(String.format((String) redditProps.get("authHeader"), this.accessToken));
        this.setRefreshTokenUrl(
                String.format((String) redditProps.get("refreshTokenUrl"), this.appClientID, this.appClientSecret));
        this.setRefreshTokenData(String.format((String) redditProps.get("refreshTokenData"), this.refreshToken));
    }

    public void refreshAccessToken(String accessToken) {
        // refresh file on disk
        Writer writer = null;
        try {
            String path = Configuration.class.getClassLoader().getResource("application-props.yml").getPath();
            File file = new File(path);
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            content = content.replaceAll(this.accessToken, accessToken);
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(content);
        } catch (IOException e) {
            log.error("Problem refreshing access token on disk");
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                log.error("Problem closing writer");
            }
        }
        // refresh in memory
        this.setAuthHeader(this.authHeader.replaceAll(this.accessToken, accessToken));
        this.setAccessToken(accessToken);
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

    public String getBaseApi() {
        return this.baseApi;
    }

    public void setBaseApi(String baseApi) {
        this.baseApi = baseApi;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Pair<String, String>> getDiscordReacts() {
        return this.discordReacts;
    }

    public void setDiscordReacts(List<Pair<String, String>> discordReacts) {
        this.discordReacts = discordReacts;
    }

    public String getSourceNSFW() {
        return this.sourceNSFW;
    }

    public void setSourceNSFW(String sourceNSFW) {
        this.sourceNSFW = sourceNSFW;
    }

    public String getMeUrl() {
        return this.meUrl;
    }

    public void setMeUrl(String meUrl) {
        this.meUrl = meUrl;
    }

    public String getAuthHeader() {
        return this.authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public String getRefreshTokenUrl() {
        return this.refreshTokenUrl;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }

    public String getRefreshTokenData() {
        return this.refreshTokenData;
    }

    public void setRefreshTokenData(String refreshTokenData) {
        this.refreshTokenData = refreshTokenData;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAppClientID() {
        return this.appClientID;
    }

    public void setAppClientID(String appClientID) {
        this.appClientID = appClientID;
    }

    public String getAppClientSecret() {
        return this.appClientSecret;
    }

    public void setAppClientSecret(String appClientSecret) {
        this.appClientSecret = appClientSecret;
    }

}