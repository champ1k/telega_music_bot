package org.alvl.nix.java.telegamusicbot.utils.audioutils;


import org.alvl.nix.java.telegamusicbot.exceptions.SongNotFoundException;
import org.alvl.nix.java.telegamusicbot.model.Song;
import org.alvl.nix.java.telegamusicbot.services.SongService;
import org.alvl.nix.java.telegamusicbot.utils.botutils.BotContext;

import org.alvl.nix.java.telegamusicbot.utils.keyboardutils.KeyboardUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.alvl.nix.java.telegamusicbot.utils.botutils.BotPhrases.NO_SONG_MSG;
import static org.alvl.nix.java.telegamusicbot.utils.keyboardutils.KeyboardText.*;
import static org.alvl.nix.java.telegamusicbot.utils.keyboardutils.KeyboardUtils.*;

import java.util.*;


public class AudioDisplay {


    public static SendAudio sendRandomSong(BotContext context, SongService songService, String buttontext) throws SongNotFoundException {
        Random random = new Random();
        List<Integer> songIds = songService.findAllIds();
        Song song = songService.findSongById(songIds.get(random.nextInt(songIds.size())));
        SendAudio message = new SendAudio();
        message.setChatId(context.getUser().getChatId());
        message.setAudio(song.getFileId());
        message.setReplyMarkup(createSongKeyboard(buttontext));
        return message;
    }

    public static boolean sendAllSongsByTitle(BotContext context, SongService songService, String inputtext) {
        List<Song> songs = songService.findAllByTitle(inputtext);
        if (songs.isEmpty()) {
            return false;
        }
        songs.stream()
                .map(song -> new SendAudio().setChatId(context.getUser().getChatId()).setAudio(song.getFileId()).setCaption(song.getTitle()).setDuration(song.getDuration()))
                .forEach(song -> sendSong(context, song));
        return true;
    }

    public static SendMessage sendAllSongs(BotContext context, SongService songService) {
        List<Song> songs = songService.findAll();
        List<SendAudio> messages = new ArrayList<>(songs.size());
        SendMessage nmsg = new SendMessage();
        if (songs.isEmpty()) {
            return new SendMessage(context.getUser().getChatId(), NO_SONG_MSG);
        }
        for (int i = 0; i < songs.size(); i++) {
            StringBuilder sb = new StringBuilder();
            songs.forEach(song -> {
                sb.append("Song ").append(song.getTitle()).append("\n").append("-------------\n");
                nmsg.setChatId(context.getUser().getChatId()).setText(String.valueOf(sb));
            });
        }
        return nmsg;
    }


    public static SendAudio sendSongById(BotContext context, SongService songService, Integer id) throws SongNotFoundException {
        Song song = songService.findSongById(id);
        SendAudio message = new SendAudio();
        message.setChatId(context.getUser().getChatId());
        message.setAudio(song.getFileId());
        message.setReplyMarkup(createSongKeyboard("Open Menu"));
        return message;
    }

    private static void sendSong(BotContext context, SendAudio audio) {
        try {
            context.getBot().execute(audio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
