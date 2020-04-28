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
    private String baseApi;
    private Configuration config = new Configuration();

    public RedditIngestor(String baseApi) {
        this.baseApi = baseApi;
    }

    public List<Celeb> getHot(String after, String before, Integer count, Integer limit, String show) {
        HttpResponse<String> response = Unirest.get(baseApi + "hot.json")
                                            .queryString("limit", limit.toString())
                                            .queryString("count", count.toString())
                                            .asString();
        JSONObject responseJson = new JSONObject(response.getBody());
        if (response.getStatus() != 200) {
            log.error("Something went wrong with Reddit API call");
            return null;
        }

        List<Celeb> celebs = new ArrayList<>();
        JSONArray children = responseJson.getJSONObject("data").getJSONArray("children");
        while (celebs.size() < config.getDefaultNCelebs()) {
            int rand = (int) Math.floor(Math.random() * (children.length()));
            JSONObject jsonData = children.getJSONObject(rand).getJSONObject("data");
            Celeb celeb = new Celeb(getCelebName(jsonData), getURL(jsonData));
            if (celebs.contains(celeb)) continue;
            celebs.add(celeb);
        }
        return celebs;
    }

    private String getCelebName(JSONObject json) {
        return json.getString("title");
    }

    private String getURL(JSONObject json) {
        return json.getString("url");
    }

}