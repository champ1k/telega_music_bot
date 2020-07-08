package org.alvl.nix.java.telegamusicbot.components;

import lombok.SneakyThrows;

import org.alvl.nix.java.telegamusicbot.exceptions.UserNotFoundException;
import org.alvl.nix.java.telegamusicbot.model.Song;
import org.alvl.nix.java.telegamusicbot.model.User;
import org.alvl.nix.java.telegamusicbot.services.SongService;
import org.alvl.nix.java.telegamusicbot.services.UserService;
import org.alvl.nix.java.telegamusicbot.utils.botutils.BotContext;
import org.alvl.nix.java.telegamusicbot.utils.botutils.BotState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.alvl.nix.java.telegamusicbot.utils.botutils.BotPhrases.*;

@Component
@PropertySource("classpath:telegram.properties")
public class TelegaMusicBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegaMusicBot.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SongService songService;

    @Value("${bot.nickname}")
    private  String bot_nick ;
    @Value("${bot.token}")
    private  String bot_tok ;

    @Override
    public String getBotUsername() {
        return bot_nick;
    }

    @Override
    public String getBotToken() {
        return bot_tok;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        final Long chatId = update.getMessage().getChatId();
        final String text = update.getMessage().getText();



        if (update.getMessage().getAudio() != null) {
            saveUserSong(update, songService);
            sendMessage(chatId, ADD_SONG_MSG);
        }


        User user = userService.findUserById(update.getMessage().getFrom().getId());

        if (text != null && checkIfAdminCommand(user, text)) {
            return;
        }

        BotContext context;
        BotState state;

        if (user == null) {
            state = BotState.getInitialState();
            User newUser = new User();
            newUser.setId(update.getMessage().getFrom().getId());
            newUser.setChatId(chatId);
            newUser.setNickname(update.getMessage().getFrom().getUserName());
            newUser.setStateId(state.ordinal());
            userService.save(newUser);

            context = BotContext.of(this, newUser, text, update);
            state.enter(context,songService,update);
            logger.info("NEW USER :" + newUser.getId());
        } else {
            context = BotContext.of(this, user, text, update);
            state = BotState.byId(user.getStateId());

            logger.info("user " + user.getId() + " in state " + state);
        }

        state.handleInput(context, update, songService);

        do {
            state = state.nextState();
            logger.warn("current state " + state);
            logger.info(context.getInput());
            state.enter(context, songService, update);
        } while (!state.isBotStopped());


        context.getUser().setStateId(state.ordinal());
        userService.save(context.getUser());
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", getBotUsername(), getBotToken());
    }

    private boolean checkIfAdminCommand(User user, String text) {
        if (user != null && user.getNickname() != null && user.getNickname().equals("champ1k")) {

        } else if (user == null || !user.isAdmin()) {
            return false;
        }

        if (text.equals("/all")) {
            logger.info("Admin call getAllUsers");
            listUsers(user);
            return true;
        } else if (text.equals("/all_small")) {
            logger.info("Admin call getAllUsers");
            listUsersSmall(user);
            return true;
        } else if (text.startsWith("/broadcast ")) {
            logger.info("Admin call broadcast");
            text = text.substring("/broadcast ".length());
            broadcast(text);
            return true;
        } else if (text.startsWith("/addadmin ")) {
            logger.info("Admin call addadmin");
            text = text.substring("/addadmin ".length());
            addadmin(user, text);
            return true;
        } else if (text.startsWith("/removeadmin ")) {
            logger.info("Admin call removeadmin");

            text = text.substring("/removeadmin ".length());
            removeadmin(user, text);
            return true;
        } else if (text.startsWith("/admins")) {
            logger.info("Admin call admins");
            admins(user);
            return true;
        } else if (text.startsWith("/userinfo")) {
            logger.info("Admin call userinfo");
            try {
                text = text.substring("/userinfo ".length());
            } catch (StringIndexOutOfBoundsException e) {
                sendMessage(user.getChatId(), "Set chat id, please");
            }
            userinfo(user, text);
            return true;
        } else if (text.startsWith("/help")) {
            logger.info("Admin call removeadmin");
            help(user);
            return true;
        }

        return false;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error while sending the message", e);
        }
    }

    private void listUsers(User admin) {
        List<User> users = userService.findAll();
        int countPerMsg = 5;
        for (int i = 0; i < users.size() / countPerMsg + 1; i++) {
            StringBuilder sb = new StringBuilder();
            int lastIndexUser = i * countPerMsg + countPerMsg;
            if (lastIndexUser > users.size()) {
                lastIndexUser = users.size() - 1;
            }
            sb.append("--- Page ").append(i + 1).append(" ---\n");
            users.subList(i * countPerMsg, lastIndexUser).forEach(user -> {
                sb.append("User  ").append(user.toString()).append("\n").append("-------------\n");
            });
            sendMessage(admin.getChatId(), sb.toString());
        }
    }

    private void listUsersSmall(User admin) {
        List<User> users = userService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("-------------\n");
        sb.append("Count all bot users: ").append(users.size()).append("\n");
        sb.append("-------------\n\n");
        sb.append("-ID---NAME---PHONE-").append("\n\n");
        users.forEach(user -> {
            String firstName = user.getFirstName();
            if (firstName == null) {
                firstName = "---";
            }
            sb.append(user.getId()).append(" ").append(firstName).append(" ").append("\n");
            sb.append("-------------\n");
        });
        sendMessage(admin.getChatId(), sb.toString());
    }

    private void broadcast(String text) {
        List<User> users = userService.findAll();
        users.forEach(user -> sendMessage(user.getChatId(), text));
    }


    private void addadmin(User admin, String text) {
        AtomicBoolean isFounded = new AtomicBoolean(false);
        userService.findAll().forEach(user -> {
            if (user.getNickname() != null && user.getNickname().equals(text)) {
                user.setAdmin(true);
                userService.save(user);
                isFounded.set(true);
                return;
            }
        });
        if (!isFounded.get()) {
            sendMessage(admin.getChatId(), "Person did not use the bot.");
        } else {
            sendMessage(admin.getChatId(), "User is admin.");
        }

    }

    private void removeadmin(User admin, String text) {
        AtomicBoolean isFounded = new AtomicBoolean(false);
        userService.findAll().forEach(user -> {
            if (user.getNickname().equals(text)) {
                user.setAdmin(false);
                userService.save(user);
                isFounded.set(true);
                return;
            }
        });
        if (!isFounded.get()) {
            sendMessage(admin.getChatId(), "Person did not use the bot.");
        } else {
            sendMessage(admin.getChatId(), "User is not admin.");
        }
    }

    private void admins(User admin) {
        StringBuilder sb = new StringBuilder();
        List<User> admins = userService.findAll().stream().filter(User::isAdmin).collect(Collectors.toList());
        sb.append("-------------\n");
        admins.forEach(admin1 -> {
            sb.append("Admin ").append(admin1.toString()).append("\n").append("-------------\n");
        });
        sendMessage(admin.getChatId(), sb.toString());
    }

    private void userinfo(User admin, String text) {
        try {
            Integer id = Integer.valueOf(text);
            User user = userService.findUserById(id);
            if (user == null) {
                throw new IllegalArgumentException();
            }
            String sb = "-------------\n" +
                    "User " + user.toString() + "\n" + "-------------\n";
            sendMessage(admin.getChatId(), sb);
        } catch (RuntimeException | UserNotFoundException e) {
            sendMessage(admin.getChatId(), "Chat Id is incorrect!");
        }
    }

    private void help(User admin) {
        String helpMsg = "Admins comman: \n"
                + "/all - all users.\n"
                + "/all_small - short bot users.\n"
                + "/userinfo user_id - User info by id.\n"
                + "/broadcast text - admin sends message to everyone.\n"
                + "/addadmin nickname - add admin features to user by nickname.\n"
                + "/removeadmin nickname - delete admin features from user by nickname.\n"
                + "/admins - admins list\n"
                + "/help - this message\n\n\n"
                + "Bot creator: @champ1k";
        sendMessage(admin.getChatId(), helpMsg);
    }

    private void saveUserSong(Update update, SongService songService) {
        Song newAudio = new Song();
        newAudio.setFileId(update.getMessage().getAudio().getFileId());
        newAudio.setDuration(update.getMessage().getAudio().getDuration());
        newAudio.setMimeType(update.getMessage().getAudio().getMimeType());
        newAudio.setFileSize(update.getMessage().getAudio().getFileSize());
        newAudio.setTitle(update.getMessage().getAudio().getTitle());
        newAudio.setPerformer(update.getMessage().getAudio().getPerformer());
        songService.save(newAudio);
    }
}
