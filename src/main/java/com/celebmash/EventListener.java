package com.celebmash;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * EventListener
 */
public class EventListener extends ListenerAdapter {
    private static Logger log = LoggerFactory.getLogger(EventListener.class);
    private Configuration config = new Configuration();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g.
        // console view (strip discord formatting)
        MessageChannel channel = event.getChannel();
        switch (content) {
            case "!ping":
                channel.sendMessage("Use NOED if gay").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
                break;
            
            case "!celeb":
                // call reddit ingestor
                RedditIngestor red = new RedditIngestor(config.getRedditApi());
                List<Celeb> celebs = red.getHot(null, null, 0, 100, null);
                List<Pair<String, String>> discordReacts = config.getDiscordReacts();
                StringBuilder strBuilder = new StringBuilder();
                for (int i = 0; i < celebs.size(); i++) {
                    strBuilder.append(discordReacts.get(i).getLeft());
                    strBuilder.append(" for ");
                    strBuilder.append(celebs.get(i).toString());
                    strBuilder.append("\n");
                }
                log.debug(strBuilder.toString());
                Message msg = channel.sendMessage(strBuilder.toString()).complete();
                for (int i = 0; i < celebs.size(); i++ ){
                     msg.addReaction(discordReacts.get(i).getRight()).queue();
                }

                break;
        
            default:
                break;
        }
    }

}