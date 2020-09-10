package com.celebmash;

import java.util.ArrayList;
import java.util.List;

import com.celebmash.DTO.Celeb;
import com.celebmash.config.Configuration;
import com.celebmash.config.RedditProps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class RedditIngestor {
    private Logger log = LoggerFactory.getLogger(RedditIngestor.class);
    private String baseEndpoint;
    private String celebEndpoint;
    private String celebNsfwEndpoint;
    private Configuration config;
    private RedditProps redditProps;

    public RedditIngestor(Configuration config) {
        this.config = config;
        this.redditProps = config.getReddit();
        this.baseEndpoint = redditProps.getOauthBase();
        this.celebEndpoint = redditProps.getSource();
        this.celebNsfwEndpoint = redditProps.getSourceNsfw();
    }

    public List<Celeb> getHotNsfw(Integer count, Integer limit) {
        HttpResponse<String> response = Unirest.get(baseEndpoint + celebNsfwEndpoint + "hot.json")
                .header("Authorization", redditProps.getAuthHeader()).queryString("limit", limit.toString())
                .queryString("count", count.toString()).asString();
        List<Celeb> celebs = processResponse(response);
        if (celebs == null) {
            celebs = getHotNsfw(count, limit);
        }
        return celebs;
    }

    public List<Celeb> getHot(Integer count, Integer limit) {
        HttpResponse<String> response = Unirest.get(baseEndpoint + celebEndpoint + "hot.json")
                .header("Authorization", redditProps.getAuthHeader()).queryString("limit", limit.toString())
                .queryString("count", count.toString()).asString();
        List<Celeb> celebs = processResponse(response);
        if (celebs == null) {
            celebs = getHot(count, limit);
        }
        return celebs;
    }

    private List<Celeb> processResponse(HttpResponse<String> response) {
        JSONObject responseJson = new JSONObject(response.getBody());
        if (response.getStatus() == 401 || response.getStatus() == 403) {
            log.info("Access token invalid, refreshing");
            refreshAccessToken();
            return null;
        } else if (!response.isSuccess()) {
            log.error("Something went wrong with Reddit API call, status: {}", response.getStatus());
            return null;
        } else {
            List<Celeb> celebs = new ArrayList<>();
            JSONArray children = responseJson.getJSONObject("data").getJSONArray("children");
            while (celebs.size() < config.getApp().getDefaultNCeleb()) {
                int rand = (int) Math.floor(Math.random() * (children.length()));
                JSONObject jsonData = children.getJSONObject(rand).getJSONObject("data");
                Celeb celeb = new Celeb(getCelebName(jsonData), getURL(jsonData));
                if (celebs.contains(celeb))
                    continue;
                // hide post to mark as read
                hidePost(children.getJSONObject(rand).getJSONObject("data").getString("name"));
                celebs.add(celeb);
            }
            return celebs;
        }
    }

    public void refreshAccessToken() {
        HttpResponse<String> response = Unirest.post(redditProps.getRefreshTokenUrl())
                .field("grant_type", "refresh_token").field("refresh_token", redditProps.getRefreshToken()).asString();
        if (response.isSuccess()) {
            JSONObject obj = new JSONObject(response.getBody());
            log.debug("New Access Token: {}", obj.getString("access_token"));
            config.refreshAccessToken(obj.getString("access_token"));
        } else {
            log.error("Something went wrong with refreshing token");
        }
    }

    private void hidePost(String postName) {
        HttpResponse<String> response = Unirest.post(baseEndpoint + "api/hide")
                .header("Authorization", redditProps.getAuthHeader()).queryString("id", postName).asString();
        log.debug("Hide post status: {}", response.getStatus());
    }

    private String getCelebName(JSONObject json) {
        return json.getString("title");
    }

    private String getURL(JSONObject json) {
        return json.getString("url");
    }

}