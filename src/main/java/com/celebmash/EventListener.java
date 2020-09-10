package com.celebmash;

import java.util.ArrayList;
import java.util.List;

import com.celebmash.DTO.Celeb;
import com.celebmash.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * EventListener
 */
public class EventListener extends ListenerAdapter {
    private static Logger log = LoggerFactory.getLogger(EventListener.class);
    private Configuration config;
    private RedditIngestor redditIngestor;

    public EventListener(Configuration config) {
        this.config = config;
        redditIngestor = new RedditIngestor(config);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return; // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g.
        // console view (strip discord formatting)
        MessageChannel channel = event.getChannel();
        content = content.toLowerCase();
        RedditIngestor reddit = null;
        List<Celeb> celebs = null;
        switch (content) {
            case "!ping":
                channel.sendMessage("Use NOED if gay").queue(); // Important to call .queue() on the RestAction returned
                                                                // by sendMessage(...)
                break;

            case "!celeb":
                celebs = reddit.getHot(null, null, 0, 100, null);
                send(channel, celebs.size(), parseToMessage(celebs));
                break;

            case "!celebnsfw":
                celebs = reddit.getHot(null, null, 0, 100, null);
                send(channel, celebs.size(), parseToMessage(celebs));

            default:
                break;
        }
    }

    private String parseToMessage(List<Celeb> celebs) {
        List<String> reacts = new ArrayList<>(config.getDiscord().getReacts().keySet());
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < celebs.size(); i++) {
            strBuilder.append(reacts.get(i));
            strBuilder.append(" for ");
            strBuilder.append(celebs.get(i).toString());
            strBuilder.append("\n");
        }
        log.debug(strBuilder.toString());
        return strBuilder.toString();
    }

    private void send(MessageChannel channel, int nCelebs, String message) {
        Message msg = channel.sendMessage(message).complete();
        for (int i = 0; i < nCelebs; i++) {
            msg.addReaction(config.getDiscord().getReacts().get(i)).queue();
        }
    }

}