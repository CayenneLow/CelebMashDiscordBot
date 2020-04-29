package com.celebmash;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class RedditIngestor {
    private Logger log = LoggerFactory.getLogger(RedditIngestor.class);
    private String endpoint;
    private Configuration config = new Configuration();

    public RedditIngestor(String source) {
        this.endpoint = config.getBaseApi() + source;
    }

    public List<Celeb> getHot(String after, String before, Integer count, Integer limit, String show) {
        HttpResponse<String> response = Unirest.get(endpoint + "hot.json")
                .header("Authorization", config.getAuthHeader()).queryString("limit", limit.toString())
                .queryString("count", count.toString()).asString();
        JSONObject responseJson = new JSONObject(response.getBody());
        if (response.getStatus() == 401 || response.getStatus() == 403) {
            log.info("Access token invalid, refreshing");
            refreshAccessToken();
            return getHot(after, before, count, limit, show);
        } else if (!response.isSuccess()) {
            log.error("Something went wrong with Reddit API call, status: {}", response.getStatus());
            return null;
        } else {
            List<Celeb> celebs = new ArrayList<>();
            JSONArray children = responseJson.getJSONObject("data").getJSONArray("children");
            while (celebs.size() < config.getDefaultNCelebs()) {
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
        HttpResponse<String> response = Unirest.post(config.getRefreshTokenUrl()).field("grant_type", "refresh_token")
                .field("refresh_token", config.getRefreshToken()).asString();
        if (response.isSuccess()) {
            JSONObject obj = new JSONObject(response.getBody());
            log.debug("New Access Token: {}", obj.getString("access_token"));
            config.refreshAccessToken(obj.getString("access_token"));
        } else {
            log.error("Something went wrong with refreshing token");
        }
    }

    private void hidePost(String postName) {
        HttpResponse<String> response = Unirest.post(endpoint + "api/hide").header("Authorization", config.getAuthHeader()).queryString("id", postName).asString();
        log.debug("Hide post status: {}", response.getStatus());
    }

    private String getCelebName(JSONObject json) {
        return json.getString("title");
    }

    private String getURL(JSONObject json) {
        return json.getString("url");
    }

}