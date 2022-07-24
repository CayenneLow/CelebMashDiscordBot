package com.celebmash.config;

import java.util.List;

public class DiscordProps {
        private String botToken;
        private List<String> reacts;

        public String getBotToken() {
            return this.botToken;
        }

        public void setBotToken(String botToken) {
            this.botToken = botToken;
        }

        public List<String> getReacts() {
            return this.reacts;
        }

        public void setReacts(List<String> reacts) {
            this.reacts = reacts;
        }
    }
