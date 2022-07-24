package com.celebmash.config;

import java.util.Map;

public class DiscordProps {
        private String botToken;
        private Map<String, String> reacts;

        public String getBotToken() {
            return this.botToken;
        }

        public void setBotToken(String botToken) {
            this.botToken = botToken;
        }

        public Map<String, String> getReacts() {
            return this.reacts;
        }

        public void setReacts(Map<String, String> reacts) {
            this.reacts = reacts;
        }
    }
