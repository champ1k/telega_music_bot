package org.alvl.nix.java.telegamusicbot.utils.botutils;

import org.alvl.nix.java.telegamusicbot.model.Song;
import org.alvl.nix.java.telegamusicbot.services.SongService;
import org.alvl.nix.java.telegamusicbot.utils.audioutils.AudioDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.alvl.nix.java.telegamusicbot.utils.botutils.BotPhrases.*;
import static org.alvl.nix.java.telegamusicbot.utils.keyboardutils.KeyboardText.*;
import static org.alvl.nix.java.telegamusicbot.utils.keyboardutils.KeyboardUtils.*;

@Component
public enum BotState {

    START {
        private BotState next;

        @Override
        public void enter(BotContext context, SongService songService, Update update) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(MENU_TEXT.getAnswers()));
            sendMessage(context, HELLO_MSG);
        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) {
            List<String> answers = MENU_REPLY_KEYBOARD_TEXT.getAnswers();
            if (context.getInput().equals("Start Using Bot")) {
                next = MAIN_MENU;
            } else {
                next = START;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    MAIN_MENU {
        private BotState next;

        @Override
        public void enter(BotContext context, SongService songService, Update update) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(MENU_REPLY_KEYBOARD_TEXT.getAnswers()));
            sendMessage(context, MAIN_MENU_MSG);
        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) {
            switch (context.getInput()) {
                case "Find Song":
                    next = FIND_MUSIC;
                    break;
                case "Playlist":
                    next = PLAY_LIST;
                    break;
                case "Help":

                    next = HELP;
                    break;
                case "Random Song":

                    next = RANDOM_SONG;
                    break;
                default:
                    next = WAITING;
                    break;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    FIND_MUSIC {
        private BotState next;

        @Override
        public void enter(BotContext context, SongService songService, Update update) throws TelegramApiException {

            context.setReplyKeyboard(createMenuKeyboard("Open Menu"));
            sendMessage(context, SONG_MSG);
        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) throws TelegramApiException {
            if (context.getInput().equals("Open Menu")) {
                next = MAIN_MENU;
            } else if (!context.getInput().isEmpty()) {
                if (!AudioDisplay.sendAllSongsByTitle(context, songService, context.getInput())) {
                    context.getBot().execute(new SendMessage(context.getUser().getChatId(), NO_SONG_MSG));
                }
                next = FIND_MUSIC;
            } else next = WAITING;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    PLAY_LIST {
        private BotState next;

        @Override
        public void enter(BotContext context, SongService songService, Update update) throws TelegramApiException {
            context.getBot().execute(AudioDisplay.sendAllSongs(context, songService));
            context.setReplyKeyboard(createMenuKeyboard("Open Menu"));
            sendMessage(context, SONG_MSG);
        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) throws TelegramApiException {
            if (context.getInput().equals("Open Menu")) {
                next = MAIN_MENU;
            } else if (!context.getInput().isEmpty()) {
                if (!AudioDisplay.sendAllSongsByTitle(context, songService, context.getInput())) {
                    context.getBot().execute(new SendMessage(context.getUser().getChatId(), NO_SONG_MSG));
                }
                next = FIND_MUSIC;
            } else next = WAITING;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    HELP {
        private BotState next;

        @Override
        public void enter(BotContext context, SongService songService, Update update) {
            context.setReplyKeyboard(createMenuKeyboard("Open Menu"));
            sendMessage(context, HELP_MSG);

        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) {
            if (context.getInput().equals("Open Menu")) {
                next = MAIN_MENU;
            } else {
                next = WAITING;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    RANDOM_SONG {
        private BotState next;
        private Integer messageId;

        @Override
        public void enter(BotContext context, SongService songService, Update update) throws TelegramApiException {
            Message execute = context.getBot().execute(AudioDisplay.sendRandomSong(context, songService, "Menu"));
            messageId = execute.getMessageId();
        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) throws TelegramApiException {
            next = MAIN_MENU;
            if (context.getInput().equals("\uD83D\uDC97")) {
                context.getBot().execute(deleteMessage(update));
            } else if (context.getInput().equals("\uD83D\uDC94")) {
                context.getBot().execute(deleteMessage(update));
                context.getBot().execute(deleteMessage(messageId, update));
                next = RANDOM_SONG;
            }
        }


        @Override
        public BotState nextState() {
            return next;
        }
    },
    WAITING {
        private BotState next;

        @Override
        public void enter(BotContext context, SongService songService, Update update) {
            context.setReplyKeyboard(createMenuKeyboard("Open Menu"));
            sendMessage(context, WAITING_MSG);
        }

        @Override
        public void handleInput(BotContext context, Update update, SongService songService) {
            if (context.getInput().equals("Open Menu")) {
                next = MAIN_MENU;
            } else {
                next = WAITING;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    };


    private static BotState[] states;
    private boolean botStopped;
    private static final Logger logger = LoggerFactory.getLogger(BotState.class);


    BotState() {
        botStopped = true;
    }

    BotState(boolean botStopped) {
        this.botStopped = botStopped;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);
        message.setReplyMarkup(context.getReplyKeyboard());
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error while sending the message", e);
        }
    }

    protected DeleteMessage deleteMessage(Integer messageId, Update update) {

        Long chat_id = update.getMessage().getChatId();
        DeleteMessage deleteMessage = new DeleteMessage();
        logger.warn(String.valueOf(chat_id));
        deleteMessage.setChatId(chat_id);
        deleteMessage.setMessageId(messageId);
        return deleteMessage;
    }

    protected DeleteMessage deleteMessage(Update update) {
        Integer messageId = update.getMessage().getMessageId();
        Long chat_id = update.getMessage().getChatId();
        DeleteMessage deleteMessage = new DeleteMessage();
        logger.warn(String.valueOf(chat_id));
        deleteMessage.setChatId(chat_id);
        deleteMessage.setMessageId(messageId);
        return deleteMessage;
    }


    public boolean isBotStopped() {
        return botStopped;
    }

    public void handleInput(BotContext context, Update update, SongService songService) throws TelegramApiException {
        //impl in childes
    }

    public abstract void enter(BotContext context, SongService songService, Update update) throws TelegramApiException;

    public abstract BotState nextState();


}