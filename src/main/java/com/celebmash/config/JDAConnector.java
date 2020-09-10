package com.celebmash.config;

import javax.security.auth.login.LoginException;

import com.celebmash.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class JDAConnector {
    private static Logger log = LoggerFactory.getLogger(JDAConnector.class);
    private Configuration config;

    public JDAConnector(Configuration config) {
        this.config = config;
    }

    public void connect() {
        try {
            JDA jda = JDABuilder.createDefault(config.getDiscord().getBotToken()).build();
            jda.addEventListener(new EventListener(config));
        } catch (LoginException e) {
            log.error(e.getMessage());
        }
    }
}
