package org.alvl.nix.java.telegamusicbot.utils.botutils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.alvl.nix.java.telegamusicbot.components.TelegaMusicBot;
import org.alvl.nix.java.telegamusicbot.model.User;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@AllArgsConstructor
@Getter
@Setter
public class BotContext {
    private final TelegaMusicBot bot;
    private final User user;
    private final String input;
    private final Update update;
    private ReplyKeyboard replyKeyboard;


    public BotContext(TelegaMusicBot bot, User user, String input, Update update) {
        this.bot = bot;
        this.user = user;
        this.input = input;
        this.update = update;
    }

    public static BotContext of(TelegaMusicBot bot, User user, String text, Update update) {
        return new BotContext(bot, user, text, update);
    }
}
